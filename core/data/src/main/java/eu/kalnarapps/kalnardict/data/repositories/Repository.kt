package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.datasources.WordDataSource
import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutorProvider
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainOperationalMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionary
import eu.kalnarapps.kalnardict.data.mapper.toExternalDatabaseTable
import eu.kalnarapps.kalnardict.data.mapper.toExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.mapper.toImportEntry
import eu.kalnarapps.kalnardict.data.model.NewDictionary
import eu.kalnarapps.kalnardict.data.model.NewTranslatedWord
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class Repository(
    private val wordDataSource: WordDataSource,
    private val queryExecutorProvider: QueryExecutorProvider,
    private val dictionaryDataSource: DictionaryDataSource,
    private val externalDbHandler: ExternalDatabaseHandler,
    private val dictionaryMapper: DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary>
) : DictionaryRepository {

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        return with(
            queryExecutorProvider.provideQueryExecutor(
                matchType = query.queryMode,
                accentMode = query.accentMode
            )
        ) {
            query(
                query.queryString,
                query.dictionary.id
            )
                .map {
                    DictWord(
                        it.id,
                        query.dictionary.languageFrom,
                        baseForm = it.baseForm,
                        alternativeForm = it.alternativeBaseForm
                    )
                }
        }
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        return when (val fetchDictionary = dictionaryDataSource.getDictionaryById(dictionaryId)) {
            is DataOperationResult.Success -> {
                dictionaryMapper.toDomainModel(fetchDictionary.data)
            }
            is DataOperationResult.Failure -> {
                DataOperationResult.Failure(
                    errorMessage = "no dictionary found with id $dictionaryId in data source",
                    cause = fetchDictionary
                )
            }
        }
    }

    override suspend fun getTranslationById(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        return wordDataSource.getTranslationByWordAndDictionaryId(wordId, dictionaryId)
    }

    override suspend fun importTableFromDb(
        importJob: ImportJob
    ): Flow<DataOperationResult<ImportProgress>> = flow {
        when (
            val readRowCountResult =
                externalDbHandler.readTableRowCountFrom(importJob.toImportEntry())
        ) {
            is DataOperationResult.Success -> {
                if (readRowCountResult.data != 0) {
                    val dictionaryId = dictionaryDataSource.insertDictionary(
                        NewDictionary(
                            name = importJob.displayName,
                            languageFrom = importJob.table.languageFrom,
                            languageTo = importJob.table.languageTo
                        )
                    )
                    if (dictionaryId == -1L) {
                        emit(
                            DataOperationResult.Failure(
                                errorMessage = "an error occurred while " +
                                        "inserting table meta info: " +
                                        "$${importJob.table.name}"
                            )
                        )
                        return@flow
                    }
                    var processedRowNumbers = 0
                    while (processedRowNumbers < readRowCountResult.data) {
                        val readResult =
                            externalDbHandler.readTableEntriesFrom(
                                importJob.toImportEntry(
                                    fromId = processedRowNumbers,
                                    tillId = processedRowNumbers + importJob.batchSize
                                )
                            )
                        when (readResult) {
                            is DataOperationResult.Success -> {
                                val insertResult = wordDataSource.insertDictEntries(
                                    readResult.data.map {
                                        NewTranslatedWord(
                                            baseForm = it.baseForm,
                                            alternativeBaseForm = it.alternativeBaseForm,
                                            translation = it.translation,
                                            dictionaryId = dictionaryId.toInt()
                                        )
                                    }
                                )
                                when (insertResult) {
                                    OperationResult.Success -> {
                                        processedRowNumbers += readResult.data.size
                                        emit(
                                            DataOperationResult.Success(
                                                ImportProgress(
                                                    dictionaryId = dictionaryId.toInt(),
                                                    totalRowCount = readRowCountResult.data,
                                                    registeredCount = processedRowNumbers
                                                )
                                            )
                                        )
                                    }
                                    is OperationResult.Failure -> {
                                        emit(
                                            DataOperationResult.Failure(
                                                errorMessage = "an error occurred while " +
                                                        "importing table: " +
                                                        "$${importJob.table.name}, " +
                                                        "processed rows: $processedRowNumbers",
                                                cause = insertResult
                                            )
                                        )
                                        return@flow
                                    }
                                }.exhaustive
                            }
                            is DataOperationResult.Failure -> {
                                emit(
                                    DataOperationResult.Failure(
                                        errorMessage = "an error occurred while " +
                                                "reading table: " +
                                                "$${importJob.table.name}, " +
                                                "processed rows: $processedRowNumbers",
                                        cause = readResult
                                    )
                                )
                                return@flow
                            }
                        }.exhaustive
                    }
                }
            }
            is DataOperationResult.Failure -> {
                emit(
                    DataOperationResult.Failure(
                        errorMessage = "an error has occurred while reading table in importJob: $importJob",
                        cause = readRowCountResult
                    )
                )
                return@flow
            }
        }
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        val dbValidityCheck = externalDbHandler.checkDatabaseStructure(
            externalDatabase.toExternalDictionaryResource()
        )
        when (dbValidityCheck) {
            is OperationResult.Failure -> return DataOperationResult.Failure(
                errorMessage = "an error has occurred while reading ${externalDatabase.uri}",
                cause = dbValidityCheck
            )
            OperationResult.Success -> {
                // continue
            }
        }
        return when (
            val tableInfoFetch = externalDbHandler.readTableInfosFrom(
                externalDatabase.toExternalDictionaryResource()
            )
        ) {
            is DataOperationResult.Success -> {
                DataOperationResult.Success(
                    tableInfoFetch.data.map {
                        it.toExternalDatabaseTable()
                    }
                )
            }
            is DataOperationResult.Failure -> {
                DataOperationResult.Failure(
                    errorMessage = "an error has occurred while reading ${externalDatabase.uri}",
                    cause = tableInfoFetch
                )
            }
        }
    }

    override fun readRegisteredDictionaries(): Flow<List<Dictionary>> {
        return dictionaryDataSource.getDictionaries().map {
            it.map { dictLogEntryData -> dictLogEntryData.toDictionary() }
        }
    }

    override suspend fun getDictionaries(): DataOperationResult<List<Dictionary>> {
        return DataOperationResult.Success(
            dictionaryDataSource.getDictionariesOneShot().map { dictLogEntryData ->
                dictLogEntryData.toDictionary()
            }
        )
    }

    override suspend fun deleteDictionaryById(dictionaryId: Int): OperationResult {
        return dictionaryDataSource.deleteDictionaryById(dictionaryId)
    }

}


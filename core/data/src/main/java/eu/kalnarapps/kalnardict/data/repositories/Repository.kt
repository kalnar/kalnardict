package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainOperationalMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry
import eu.kalnarapps.kalnardict.data.mapper.toExternalDatabaseTable
import eu.kalnarapps.kalnardict.data.model.TableInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import java.util.Locale

class Repository(
    private val dictDao: DictDao,
    private val externalDbHandler: ExternalDatabaseHandler,
    private val dictionaryMapper: DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary>
) : DictionaryRepository {

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        return when (query.queryMode) {
            QueryMode.MATCH_ANYWHERE -> {
                dictDao.queryWithMatchAnyWhereInDictionary(
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
    }

    // TODO: test getDictionaryById
    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        return when (val fetchDictionary = dictDao.getDictionaryById(dictionaryId)) {
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
        return dictDao.getTranslationByWordAndDictionaryId(wordId, dictionaryId)
    }

    override suspend fun importTableFromDb(importJob: ImportJob): OperationResult {
        return when (
            val readResult = externalDbHandler.readTableEntriesFrom(importJob.toImportEntry())
            ) {
            is DataOperationResult.Success -> {
                if (readResult.data.isNotEmpty()) {
                    val dictionaryId = dictDao.insertDictionary(
                        NewDictionary(
                            name = importJob.table.name,
                            languageFrom = importJob.table.languageFrom,
                            languageTo = importJob.table.languageTo
                        )
                    )
                    dictDao.insertDictEntries(
                        readResult.data.map {
                            NewTranslatedWord(
                                baseForm = it.baseForm,
                                alternativeBaseForm = it.alternativeBaseForm,
                                translation = it.translation,
                                dictionaryId = dictionaryId.toInt()
                            )
                        }
                    )
                }
                OperationResult.Success
            }
            is DataOperationResult.Failure -> {
                OperationResult.Failure(
                    errorMessage = "an error has occurred while reading table in importJob: $importJob",
                    cause = readResult
                )
            }
        }
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
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

    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
        return dictDao.getDictionaries().map {
            it.toDictionary()
        }
    }

}

private fun DictionaryLogEntryData.toDictionary(): Dictionary {
    return Dictionary(
        id = id,
        languageFrom = DictLanguage(
            name = Locale(languageFrom).getDisplayLanguage(Locale(languageFrom)),
            code = languageFrom
        ),
        languageTo = DictLanguage(
            name = Locale(languageTo).getDisplayLanguage(Locale(languageTo)),
            code = languageTo
        ),
        description = name
    )
}

private fun ImportJob.toImportEntry(): ImportEntry {
    return object : ImportEntry {
        override fun externalDictionaryResource(): ExternalDictionaryResource =
            resource.toExternalDictionaryResource()

        override fun tableInfo(): ImportEntry.TableInfo = table.toTableInfo()

    }
}

private fun ExternalDatabaseTable.toTableInfo(): ImportEntry.TableInfo {
    return TableInfo(
        name = name,
        languageFrom = languageFrom,
        languageTo = languageTo
    )
}

private fun ExternalDatabase.toExternalDictionaryResource(): ExternalDictionaryResource {
    return object : ExternalDictionaryResource {
        override fun sdCardPath(): String = uri
    }
}

data class NewDictionary(
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : NewDictionaryLogEntryData

data class NewTranslatedWord(
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : TranslatedWordInsertEntry

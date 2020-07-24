package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class StubDictionaryRepository : DictionaryRepository {
    private val dictionaries = ArrayList<Dictionary>()

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        TODO("Not yet implemented")
    }

    override suspend fun importTableFromDb(importJob: ImportJob): Flow<DataOperationResult<ImportProgress>> {
        val languageFrom =
            Stubs.Languages.frenchAndEnglish.find { importJob.table.languageFrom == it.code }
        val languageTo =
            Stubs.Languages.frenchAndEnglish.find { importJob.table.languageTo == it.code }
        return flow {
            if (languageFrom != null && languageTo != null) {
                dictionaries.add(
                    Dictionary(
                        id = dictionaries.size + 1,
                        languageFrom = languageFrom,
                        languageTo = languageTo,
                        description = importJob.displayName
                    )
                )
                emit(
                    DataOperationResult.Success(
                        ImportProgress(
                            10, 10
                        )
                    )
                )
            } else {
                emit(
                    DataOperationResult.Failure(
                        errorMessage = "invalid language specified"
                    )
                )
            }
        }
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        return when (externalDatabase.uri) {
            Stubs.Uris.valid -> DataOperationResult.Success(
                listOf(
                    Stubs.MetaInfoOnDb.table1,
                    Stubs.MetaInfoOnDb.table2
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "${externalDatabase.uri} does not contain valid tables"
            )
        }
    }

    override fun readRegisteredDictionaries(): Flow<List<Dictionary>> {
        return flowOf(dictionaries)
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        TODO("Not yet implemented")
    }

    override suspend fun getTranslationById(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        TODO("Not yet implemented")
    }

}


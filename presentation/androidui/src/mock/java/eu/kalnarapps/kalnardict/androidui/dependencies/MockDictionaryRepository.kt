package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import org.koin.dsl.module


val dictionaryRepositoryModule = module {
    single {
        object : DictionaryRepository {
            override suspend fun insertDictEntry(dictTranslation: DictTranslation) {

            }

            override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
                return (1..11).map {
                    DictWord(
                        id = it,
                        language = DictLanguage(
                            name = "English",
                            code = "en"
                        ),
                        baseForm = "mock test dict entry #$it"
                    )
                }
            }

            override suspend fun importTablesFromDb(importJob: ImportJob): OperationResult {
                return OperationResult.Success
            }

            override suspend fun readRegisteredDictionaries(): List<Dictionary> {
                return (1..8).map {
                    Dictionary(
                        id = it,
                        languageFrom = DictLanguage(
                            name = "Hungarian",
                            code = "hu"
                        ),
                        languageTo = DictLanguage(
                            name = "French",
                            code = "fr"
                        ),
                        description = "mock test dict #$it"
                    )
                }
            }
        } as DictionaryRepository
    }
}
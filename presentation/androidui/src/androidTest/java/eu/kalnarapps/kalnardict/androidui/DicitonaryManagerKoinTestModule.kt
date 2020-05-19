package eu.kalnarapps.kalnardict.androidui

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinTestModule: Module = module {
    factory {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(
                object : DictionaryRepository {
                    override suspend fun insertDictEntry(dictTranslation: DictTranslation) {

                    }

                    override suspend fun getEntriesByQuery(query: DictQuery): List<DictTranslation> {
                        return emptyList()
                    }

                    override suspend fun importTablesFromDb(importJob: ImportJob): OperationResult {
                        return OperationResult.Success
                    }

                    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
                        return (1 until 10).map {
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
                }
            )
        )
    }
}
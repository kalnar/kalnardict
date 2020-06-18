package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.android.utils.KalnarLogger
import eu.kalnarapps.kalnardict.android.utils.Logger
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModel
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryQueryMockKoinModule: Module = module {
    viewModel {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = ListDictionaryQueryResults(
                get(),
//                object : DictionaryRepository {
//                    override suspend fun insertDictEntry(dictTranslation: DictTranslation) {
//
//                    }
//
//                    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
//                        return (1..11).map {
//                            DictWord(
//                                id = it,
//                                language = DictLanguage(
//                                    name = "English",
//                                    code = "en"
//                                ),
//                                baseForm = "mock test dict entry #$it"
//                            )
//                        }
//                    }
//
//                    override suspend fun importTablesFromDb(importJob: ImportJob): OperationResult {
//                        return OperationResult.Success
//                    }
//
//                    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
//                        return (1..8).map {
//                            Dictionary(
//                                id = it,
//                                languageFrom = DictLanguage(
//                                    name = "Hungarian",
//                                    code = "hu"
//                                ),
//                                languageTo = DictLanguage(
//                                    name = "French",
//                                    code = "fr"
//                                ),
//                                description = "mock test dict #$it"
//                            )
//                        }
//                    }
//                },
                configurationRepository = object : ConfigurationRepository {
                    override suspend fun getCurrentLanguage(): DictLanguage = DictLanguage(
                        name = "English",
                        code = "en"
                    )

                    override suspend fun getCurrentAccentMode(): AccentMode =
                        AccentMode.ACCENT_SENSITIVE
                }
            ),
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(get())
        )
    }
    single { KalnarLogger() as Logger }
}
package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModel
import eu.kalnarapps.kalnardict.interactors.GetCurrentLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryQueryKoinMockModule: Module = module {
    viewModel {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = ListDictionaryQueryResults(
                dictionaryRepository = get(),
                configurationRepository = get()
            ),
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(get()),
            updateCurrentLanguageUseCase = UpdateCurrentLanguage(
                dictionaryRepository = get(),
                configurationRepository = get()
            ),
            getCurrentLanguageUseCase = GetCurrentLanguageUseCase(
                configurationRepository = get()
            ),
            getTranslation = get()
        )
    }
}
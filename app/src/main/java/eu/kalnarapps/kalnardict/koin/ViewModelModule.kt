package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val viewModuleModule: Module = module {

    viewModel {
        DictionaryManagerViewModel(listRegisteredDictionariesUseCase = get())
    }
    viewModel {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = get(),
            listRegisteredDictionariesUseCase = get(),
            updateCurrentLanguageUseCase = get(),
            getCurrentLanguageUseCase = get()
        )
    }
}

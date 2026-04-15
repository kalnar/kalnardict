package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesForUi
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val dictionaryManagerKoinMockModule: Module = module {
    viewModel {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = ListManageableDictionariesForUi(
                get(),
                get(),
                get()
            ),
            uiLogger = get(),
            updateRenderingStrategy = get(),
            deleteDictionary = get()
        )
    }
}
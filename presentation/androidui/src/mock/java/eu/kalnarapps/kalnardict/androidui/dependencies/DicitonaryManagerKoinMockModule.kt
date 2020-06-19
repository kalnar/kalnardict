package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinMockModule: Module = module {
    viewModel {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(
                get()
            )
        )
    }
}
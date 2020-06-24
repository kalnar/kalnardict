package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.RegisterNewDictionary
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinModule: Module = module {
    viewModel {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(
                get()
            )
        )
    }
}
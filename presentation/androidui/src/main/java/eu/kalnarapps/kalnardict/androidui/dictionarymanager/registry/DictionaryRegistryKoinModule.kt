package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val dictionaryRegistryKoinModule = module {
    viewModel { (dbPath: String) ->
        DictionaryRegistryViewModel(
            dbPath,
            get(),
            get()
        )
    }
}
package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockRegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModel
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val dictionaryRegistryKoinMockModule = module {
    viewModel { (dbPath: String) ->
        DictionaryRegistryViewModel(
            dbPath,
            ReadExternalDbUseCaseMock(isAlwaysValid = true),
            MockRegisterNewDictionaryUseCase()
        )
    }
}
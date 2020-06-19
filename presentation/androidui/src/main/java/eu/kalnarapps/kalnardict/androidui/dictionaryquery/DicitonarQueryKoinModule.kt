package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import eu.kalnarapps.kalnardict.android.utils.KalnarLogger
import eu.kalnarapps.kalnardict.android.utils.Logger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinModule: Module = module {
    viewModel {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = get(),
            listRegisteredDictionariesUseCase = get(),
            updateCurrentLanguageUseCase = get(),
            getCurrentLanguageUseCase = get()
        )
    }
    single { KalnarLogger() as Logger }
}
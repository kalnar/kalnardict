package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinModule: Module = module {
    factory {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = get(),
            listRegisteredDictionariesUseCase = get()
        )
    }
}
package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModuleModule: Module = module {

    viewModel {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = get(),
            uiLogger = get()
        )
    }

}

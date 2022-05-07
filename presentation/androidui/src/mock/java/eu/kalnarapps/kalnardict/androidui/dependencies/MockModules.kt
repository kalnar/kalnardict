package eu.kalnarapps.kalnardict.androidui.dependencies

import androidx.lifecycle.viewmodel.compose.viewModel
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.importer.DbImporterViewModel
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val androidUiKoinMockModules = listOf(
    useCaseKoinMockModule,
    dictionaryManagerKoinMockModule,
    navigationScreenKoinMockModule
)

// for dictionaryQueryFactory, see DictionaryQueryViewModelFactory

val uiMockModules = listOf(
    module {
        viewModel {
           DbImporterViewModel(dispatcherProvider = DefaultDispatcherProvider, get())
        }
    }
)
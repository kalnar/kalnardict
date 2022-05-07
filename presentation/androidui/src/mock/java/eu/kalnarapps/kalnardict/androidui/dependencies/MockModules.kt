package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
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
           DbImporterViewModel(
               dispatcherProvider = DefaultDispatcherProvider,
               logger = get(),
               createMockTable = get(),
               getAvailableLanguages = get(),
               getDatabases = get()
           )
        }
    }
)
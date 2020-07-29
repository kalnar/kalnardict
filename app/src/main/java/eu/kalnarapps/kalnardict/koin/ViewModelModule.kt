package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.AbstractDictionaryRegistryViewModelFactory
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModuleModule: Module = module {

    viewModel {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = get(),
            updateRenderingStrategy = get(),
            uiLogger = get()
        )
    }

    // factories
    factory { (dbPath: String) ->
        DictionaryRegistryViewModelFactory(
            dbPath = dbPath,
            loadDbMetaInfoOnDb = get(),
            registerNewDictionary = get(),
            listAvailableLanguages = get(),
            languageDomainMapper = get(Qualifiers.languageDomainUiMapper),
            languageUiMapper = get(Qualifiers.languageUiDomainMapper),
            addNewLanguage = get(),
            dispatcherProvider = DefaultDispatcherProvider,
            uiLogger = get()
        ) as AbstractDictionaryRegistryViewModelFactory
    }

}

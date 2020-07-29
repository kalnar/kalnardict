package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper

class DictionaryRegistryViewModelFactory(
    private val dbPath: String,
    private val loadDbMetaInfoOnDb: ReadExternalDbUseCase,
    private val registerNewDictionary: RegisterNewDictionaryUseCase,
    private val listAvailableLanguages: ListRegisteredLanguagesUseCase,
    private val addNewLanguage: RegisterLanguageUseCase,
    private val languageDomainMapper: DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>,
    private val languageUiMapper: UiToDomainMapper<SelectableLanguage.LanguageUi, DictLanguage>,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val uiLogger: UiLogger
) : AbstractDictionaryRegistryViewModelFactory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryRegistryViewModel::class.java)) {
            return DictionaryRegistryViewModel(
                dbPath = dbPath,
                loadDbMetaInfoOnDb = loadDbMetaInfoOnDb,
                registerNewDictionary = registerNewDictionary,
                listAvailableLanguages = listAvailableLanguages,
                languageDomainMapper = languageDomainMapper,
                languageUiMapper = languageUiMapper,
                addNewLanguage = addNewLanguage,
                dispatcherProvider = dispatcherProvider,
                uiLogger = uiLogger
            ) as T
        } else {
            throw IllegalArgumentException(
                "view model was created with the wrong factory"
            )
        }
    }
}

interface AbstractDictionaryRegistryViewModelFactory : ViewModelProvider.Factory
package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetExternalDbInfoUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.RegisterNewDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.RegisterLanguageUseCaseFromUi

class DictionaryRegistryViewModelFactory(
    private val dbPath: String,
    private val loadDbMetaInfoOnDb: GetExternalDbInfoUseCaseForUi,
    private val registerNewDictionary: RegisterNewDictionaryUseCaseFromUi,
    private val listAvailableLanguages: ListRegisteredLanguagesUseCaseForUi,
    private val addNewLanguage: RegisterLanguageUseCaseFromUi,
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
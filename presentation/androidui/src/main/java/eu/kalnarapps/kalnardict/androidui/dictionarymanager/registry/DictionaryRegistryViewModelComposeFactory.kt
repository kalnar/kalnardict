package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DictionaryRegistryViewModelComposeFactory(
    private val dbPath: String
) : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryRegistryViewModel::class.java)) {
            return DictionaryRegistryViewModel(
                dbPath = dbPath,
                loadDbMetaInfoOnDb = get(),
                registerNewDictionary = get(),
                listAvailableLanguages = get(),
                addNewLanguage = get(),
                uiLogger = get()
            ) as T
        }
        throw IllegalArgumentException("view model was created with the wrong factory")
    }
}

package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.KoinComponent

class DictionaryRegistryViewModelFactory(
    private val dbPath: String
) : ViewModelProvider.Factory, KoinComponent {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryRegistryViewModel::class.java)) {
            return DictionaryRegistryViewModel(
                dbPath = dbPath,
                loadDbMetaInfoOnDb = getKoin().get(),
                registerNewDictionary = getKoin().get()
            ) as T
        } else {
            throw IllegalArgumentException(
                "view model was created with the wrong factory"
            )
        }
    }
}
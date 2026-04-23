package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DictionaryManagerViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryManagerViewModel::class.java)) {
            return DictionaryManagerViewModel(
                listRegisteredDictionariesUseCase = get(),
                updateRenderingStrategy = get(),
                deleteDictionary = get(),
                uiLogger = get()
            ) as T
        } else {
            throw IllegalArgumentException("view model was created with the wrong factory")
        }
    }
}

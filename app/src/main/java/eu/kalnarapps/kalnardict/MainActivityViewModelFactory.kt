package eu.kalnarapps.kalnardict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivityViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(
                listRegisteredDictionaries = get()
            ) as T
        } else {
            throw IllegalArgumentException("view model was created with the wrong factory")
        }
    }
}

package eu.kalnarapps.kalnardict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivityV2ViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityV2ViewModel::class.java)) {
            return MainActivityV2ViewModel(
                listRegisteredDictionaries = get()
            ) as T
        } else {
            throw IllegalArgumentException("view model was created with the wrong factory")
        }
    }
}

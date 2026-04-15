package eu.kalnarapps.kalnardict.androidui.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PermissionErrorViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PermissionErrorViewModel::class.java)) {
            return PermissionErrorViewModel(
                listRegisteredDictionaries = get(),
                dispatcherProvider = DefaultDispatcherProvider
            ) as T
        } else {
            throw IllegalArgumentException("view model was created with the wrong factory")
        }
    }
}

package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.importer.DbImporterViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DbImporterViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DbImporterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DbImporterViewModel(
                dispatcherProvider = DefaultDispatcherProvider,
                logger = get(),
                createMockTable = get(),
                getAvailableLanguages = get(),
                getDatabases = get()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
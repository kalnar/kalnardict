package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.get

@ExperimentalCoroutinesApi
class DictionaryQueryViewModelFactory() : ViewModelProvider.Factory, KoinComponent {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryQueryViewModel::class.java)) {
            return DictionaryQueryViewModel(
                listQueryResultsUseCase = get(),
                listRegisteredDictionariesUseCase = get(),
                updateCurrentLanguageUseCase = get(),
                getCurrentLanguageUseCase = get(),
                updateQueryModeUseCase = get(),
                getQueryModesForUi = get(),
                getTranslation = get(),
                uiLogger = get()
            ) as T
        } else {
            throw IllegalArgumentException(
                "view model was created with the wrong factory"
            )
        }
    }
}
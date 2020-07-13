package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryManagerViewModel(
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    uiLogger: UiLogger
) : BaseViewModel<DictionaryManagerState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {

    init {
        viewModelScope.launch {
            setUiState(
                DictionaryManagerState(
                    loadDictionaries()
                )
            )
        }
    }

    private suspend fun loadDictionaries(): List<ManageableDictionaryView> {
        return listRegisteredDictionariesUseCase.invoke().map {
            ManageableDictionaryView(
                dictionaryName = it.description,
                sourceLanguage = it.languageFrom.name,
                destinationLanguage = it.languageTo.name
            )
        }
    }

    fun getRegisteredDictionaries(): List<ManageableDictionaryView> {
        return state.value?.dictionaries ?: emptyList()
    }

    fun onDbSelected(uriPath: String) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                postNavigationCommand(
                    NavigationCommand.NavigateToDictionaryRegistry(
                        uri = uriPath
                    )
                )
            }
        }
    }
}

data class DictionaryManagerState(
    val dictionaries: List<ManageableDictionaryView>
)

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String
)
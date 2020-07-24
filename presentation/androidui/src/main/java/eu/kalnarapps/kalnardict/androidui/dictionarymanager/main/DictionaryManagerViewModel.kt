package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
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
            setUiState(DictionaryManagerState())
        }
        viewModelScope.launch {
            loadDictionaries().map {
                LoadableContent.Completed(it) as LoadableContent<List<ManageableDictionaryView>>
            }.onStart {
                emit(LoadableContent.Loading)
            }.collect {
                postUiState {
                    copy(dictionaries = it)
                }
            }
        }
    }

    private fun loadDictionaries(): Flow<List<ManageableDictionaryView>> {
        return listRegisteredDictionariesUseCase.invoke().map {
            it.map { dictionary ->
                ManageableDictionaryView(
                    dictionaryName = dictionary.description,
                    sourceLanguage = dictionary.languageFrom.name,
                    destinationLanguage = dictionary.languageTo.name
                )
            }
        }
    }

    fun getRegisteredDictionaries(): LiveData<LoadableContent<List<ManageableDictionaryView>>> {
        return state.map { it.dictionaries }
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
    val dictionaries: LoadableContent<List<ManageableDictionaryView>> = LoadableContent.UnInitialized
)

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String
)
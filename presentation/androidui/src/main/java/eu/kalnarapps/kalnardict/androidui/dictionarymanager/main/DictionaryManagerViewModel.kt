package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.presentation.interactors.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryManagerState
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DictionaryManagerViewModel(
    private val listRegisteredDictionariesUseCase: ListManageableDictionariesUseCaseForUi,
    private val updateRenderingStrategy: UpdateDictionaryUseCaseFromUi,
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
            listRegisteredDictionariesUseCase().map {
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

    fun getRegisteredDictionaries(): LiveData<LoadableContent<List<ManageableDictionaryView>>> {
        return state.map { it.dictionaries }
    }

    fun onDbSelected(uriPath: String) {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(
                    NavigationCommand.NavigateToDictionaryRegistry(
                        uri = uriPath
                    )
                )
            }
        }
    }

    fun updateDictionary(dictionaryUpdate: DictionaryUpdateUi.Info) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                updateRenderingStrategy(dictionaryUpdate)
            }
        }

    }
}


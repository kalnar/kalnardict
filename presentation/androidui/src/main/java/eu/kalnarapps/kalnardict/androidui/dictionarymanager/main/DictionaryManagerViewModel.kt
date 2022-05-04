package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryManagerState
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun onImportDbClicked() {
        viewModelScope.launch(dispatcherProvider.main()) {
            postNavigationCommand(NavigationCommand.Platform.NavigateToDbBrowser)
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


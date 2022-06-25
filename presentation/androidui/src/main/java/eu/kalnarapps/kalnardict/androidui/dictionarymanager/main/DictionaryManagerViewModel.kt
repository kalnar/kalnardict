package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.DeleteDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryManagerState
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorUiFeedBack
import eu.kalnarapps.kalnardict.presentation.models.errors.UiFeedback
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DictionaryManagerViewModel(
    private val listRegisteredDictionariesUseCase: ListManageableDictionariesUseCaseForUi,
    private val updateRenderingStrategy: UpdateDictionaryUseCaseFromUi,
    private val deleteDictionary: DeleteDictionaryUseCaseFromUi,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    uiLogger: UiLogger
) : BaseViewModel<DictionaryManagerState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {

    init {
        setUiState(DictionaryManagerState())
        viewModelScope.launch(dispatcherProvider.io()) {
            listRegisteredDictionariesUseCase.invoke().map {
                registerClickListeners(it)
                LoadableContent.Completed(it) as LoadableContent<List<ManageableDictionaryView>>
            }.collect {
                postUiState {
                    copy(dictionaries = it)
                }
            }
        }
    }

    private fun registerClickListeners(it: List<ManageableDictionaryView>) {
        it.forEach {
            it.onDeleteAction = { dictionaryId ->
                viewModelScope.launch(dispatcherProvider.io()) {
                    when (val runDelete = deleteDictionary.invoke(dictionaryId)) {
                        is OperationResult.Failure -> {
                            postError(ErrorFromUi(runDelete.errorMessage()))
                            postError(
                                ErrorFromUi(
                                    logMessage = "Delete dictionary failed for dictionary with id: $dictionaryId",
                                    ErrorUiFeedBack.ShowSnackBarWithAction(
                                        "Delete has failed",
                                        "Retry"
                                    ) {
                                        it.onDeleteAction?.invoke(dictionaryId)
                                    }
                                )
                            )
                        }
                        OperationResult.Success -> {
                            postFeedback(UiFeedback.ShowSuccessSnackBar("Dictionary deleted successfully"))
                        }
                    }
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
        viewModelScope.launch(dispatcherProvider.io()) {
            updateRenderingStrategy(dictionaryUpdate)
        }
    }
}


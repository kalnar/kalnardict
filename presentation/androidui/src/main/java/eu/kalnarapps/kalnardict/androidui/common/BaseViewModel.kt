package eu.kalnarapps.kalnardict.androidui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi
import eu.kalnarapps.kalnardict.presentation.models.errors.UiFeedback
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import org.koin.core.KoinComponent

abstract class BaseViewModel<UiModel>(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val logger: UiLogger
) : ViewModel(), KoinComponent {
    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    internal val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    private val _state: MutableLiveData<UiModel> = MutableLiveData()
    protected val state: LiveData<UiModel>
        get() = _state
    private val _error: MutableLiveData<ErrorFromUi> = MutableLiveData()
    internal val error: LiveData<ErrorFromUi>
        get() = _error

    private val _feedback: MutableLiveData<UiFeedback> = MutableLiveData()
    internal val feedback: LiveData<UiFeedback>
        get() = _feedback

    protected suspend fun postUiState(state: UiModel?) {
        withContext(dispatcherProvider.io()) {
            if (state != null) {
                val diff = StringUtils.difference(_state.value.toString(), state.toString())
                logger.d("vm", "new state: $state")
                logger.d("vm", "state diff: $diff")
                _state.postValue(state!!)
            }
        }
    }

    protected suspend fun postUiState(stateMapper: (UiModel).() -> UiModel) {
        withContext(dispatcherProvider.io()) {
            val currentState = state.value
            currentState?.let {
                val newState = stateMapper(it)
                val diff = StringUtils.difference(_state.value.toString(), newState.toString())
                logger.d("vm", "new state: $newState")
                logger.d("vm", "state diff: $diff")
                _state.postValue(newState!!)
            }
        }
    }

    protected suspend fun postUiStateOnMainThread(stateMapper: (UiModel).() -> UiModel) {
        withContext(dispatcherProvider.main()) {
            val currentState = state.value
            currentState?.let {
                val newState = stateMapper(it)
                val diff = StringUtils.difference(_state.value.toString(), newState.toString())
                logger.d("vm", "new state: $newState")
                logger.d("vm", "state diff: $diff")
                _state.value = newState
            }
        }
    }

    protected fun postError(errorFromUi: ErrorFromUi) {
        _error.postValue(errorFromUi)
    }

    protected fun postFeedback(uiFeedback: UiFeedback) {
        _feedback.postValue(uiFeedback)
    }

    protected fun setUiState(state: UiModel) {
        _state.value = state
    }

    fun resetNavigation() {
        _navigationCommand.value = NavigationCommand.Common.DoNothing
    }

    fun getUiState(): LiveData<UiModel> {
        return state
    }

    protected fun postNavigationCommand(navCommand: NavigationCommand) {
        logger.d("vm", "posting navCommand: $navCommand")
        _navigationCommand.value = navCommand
    }
}

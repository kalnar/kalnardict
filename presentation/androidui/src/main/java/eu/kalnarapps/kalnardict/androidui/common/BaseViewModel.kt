package eu.kalnarapps.kalnardict.androidui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<UiModel>(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : ViewModel() {
    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    internal val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    private val _state: MutableLiveData<UiModel> = MutableLiveData()
    protected val state: LiveData<UiModel>
        get() = _state
    private val _error: MutableLiveData<ErrorFromUi> = MutableLiveData()
    internal val error: LiveData<ErrorFromUi>
        get() = _error

    protected fun postUiState(state: UiModel?) {
        if (state != null) {
            viewModelScope.launch {
                withContext(dispatcherProvider.io()) {
                    _state.postValue(state)
                }
            }
        }
    }

    protected fun postError(errorFromUi: ErrorFromUi) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                _error.postValue(errorFromUi)
            }
        }
    }

    protected fun setUiState(state: UiModel) {
        _state.value = state
    }

    fun resetNavigation() {
        _navigationCommand.value = NavigationCommand.DoNothing
    }

    fun getUiState(): LiveData<UiModel> {
        return state
    }

    protected fun postNavigationCommand(navCommand: NavigationCommand) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                _navigationCommand.postValue(navCommand)
            }
        }
    }
}

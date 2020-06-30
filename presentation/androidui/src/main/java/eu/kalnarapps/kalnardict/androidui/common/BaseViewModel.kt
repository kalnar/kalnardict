package eu.kalnarapps.kalnardict.androidui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<UiModel> : ViewModel() {
    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    internal val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    private val _state: MutableLiveData<UiModel> = MutableLiveData()
    protected val state: LiveData<UiModel>
        get() = _state

    protected fun postUiState(state: UiModel?) {
        if (state != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _state.postValue(state)
                }
            }
        }
    }

    protected fun setUiState(state: UiModel) {
        _state.value = state
    }

    fun resetNavigation() {
        _navigationCommand.postValue(NavigationCommand.DoNothing)
    }

    fun getUiState(): LiveData<UiModel> {
        return state
    }

    protected fun postNavigationCommand(navCommand: NavigationCommand) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _navigationCommand.postValue(navCommand)
            }
        }
    }
}
package eu.kalnarapps.kalnardict.androidui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import kotlinx.coroutines.withContext
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

    protected suspend fun postUiState(state: UiModel?) {
        withContext(dispatcherProvider.io()) {
            if (state != null) {
                _state.postValue(state)
            }
        }
    }

    protected suspend fun postUiStateOnMainThread(state: UiModel?) {
        withContext(dispatcherProvider.main()) {
            if (state != null) {
                _state.value = state
            }
        }
    }

    protected fun postError(errorFromUi: ErrorFromUi) {
        _error.postValue(errorFromUi)
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
        logger.d("vm", "posting navCommand: $navCommand")
        _navigationCommand.postValue(navCommand)
    }
}

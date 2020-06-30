package eu.kalnarapps.kalnardict.androidui.navigationscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.navigationscreen.model.NavigationItemView
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NavigationViewModel : ViewModel() {

    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    internal val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand

    fun createNavCommandList(): List<NavigationItemView> {
        return listOf(
            NavigationItemView(
                name = "DictionaryQuery",
                navCommand = NavigationCommand.NavigateToDictionaryQuery
            ),
            NavigationItemView(
                name = "DictionaryManager",
                navCommand = NavigationCommand.NavigateToDictionaryManager
            ),
            NavigationItemView(
                name = "DictionaryRegistry - valid db",
                navCommand = NavigationCommand.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.validUri
                )
            ),
            NavigationItemView(
                name = "DictionaryRegistry - invalid db",
                navCommand = NavigationCommand.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.invalidUri
                )
            ),
            NavigationItemView(
                name = "dialog - table registration - ok",
                navCommand = NavigationCommand.ShowDialog.SuccessTableRegistration(
                    table = "original mock table",
                    dictionaryName = "saving name for mock table"
                )
            ),
            NavigationItemView(
                name = "dialog - table registration - fail",
                navCommand = NavigationCommand.ShowDialog.FailureTableRegistration(
                    table = "original mock table",
                    errorMessage = "import failed due to conflicting names"
                )
            )
        )
    }

    fun onScreenItemClicked(navigationItemView: NavigationItemView) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _navigationCommand.postValue(navigationItemView.navCommand)
            }
        }
    }

    fun resetNavigation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _navigationCommand.postValue(NavigationCommand.DoNothing)
            }
        }
    }
}
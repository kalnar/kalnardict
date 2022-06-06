package eu.kalnarapps.kalnardict.androidui.navigationscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.navigationscreen.model.NavigationItemView
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
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
                navCommand = NavigationCommand.Common.NavigateToDictionaryQuery
            ),
            NavigationItemView(
                name = "DictionaryManager",
                navCommand = NavigationCommand.Common.NavigateToDictionaryManager
            ),
            NavigationItemView(
                name = "MockDbImport",
                navCommand = NavigationCommand.Platform.NavigateToDbBrowser
            ),
            NavigationItemView(
                name = "DictionaryRegistry - valid db",
                navCommand = NavigationCommand.Common.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.validUri
                )
            ),
            NavigationItemView(
                name = "DictionaryRegistry - invalid db",
                navCommand = NavigationCommand.Common.NavigateToDictionaryRegistry(
                    uri = UiStubs.Uris.invalidUri
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
                _navigationCommand.postValue(NavigationCommand.Common.DoNothing)
            }
        }
    }
}
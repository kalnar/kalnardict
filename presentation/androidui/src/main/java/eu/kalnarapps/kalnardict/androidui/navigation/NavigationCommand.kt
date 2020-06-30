package eu.kalnarapps.kalnardict.androidui.navigation

import java.net.URI

sealed class NavigationCommand {
    object Back : NavigationCommand()
    class NavigateToDictionaryRegistry(val uri: URI) : NavigationCommand()
    object NavigateToDictionaryQuery : NavigationCommand()
    object NavigateToDictionaryManager : NavigationCommand()
    object DoNothing : NavigationCommand()

    sealed class ShowDialog : NavigationCommand() {
        class SuccessTableRegistration(val table: String, val dictionaryName: String) : ShowDialog()
        class FailureTableRegistration(val table: String, val errorMessage: String) : ShowDialog()
    }
}
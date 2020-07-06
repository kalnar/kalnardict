package eu.kalnarapps.kalnardict.androidui.navigation

sealed class NavigationCommand {
    object Back : NavigationCommand()
    class NavigateToDictionaryRegistry(val uri: String) : NavigationCommand()
    class NavigateToDictionaryRegistryDialog(val uri: String) : NavigationCommand()
    object NavigateToDictionaryRegistryNewLanguageDialog : NavigationCommand()
    object NavigateToDictionaryQuery : NavigationCommand()
    object NavigateToDictionaryManager : NavigationCommand()
    object DoNothing : NavigationCommand()

    sealed class ShowDialog : NavigationCommand() {
        class SuccessTableRegistration(val table: String, val dictionaryName: String) : ShowDialog()
        class FailureTableRegistration(val table: String, val errorMessage: String) : ShowDialog()
    }
}
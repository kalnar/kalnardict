package eu.kalnarapps.kalnardict.presentation.models.navigation

sealed class NavigationCommand {
    sealed class Common: NavigationCommand() {
        object Back : NavigationCommand.Common()
        data class NavigateToDictionaryRegistry(val uri: String) : NavigationCommand.Common()
        data class NavigateToDictionaryRegistryDialog(val uri: String) : NavigationCommand.Common()
        object NavigateToDictionaryRegistryNewLanguageDialog : NavigationCommand.Common()
        object NavigateToDictionaryQuery : NavigationCommand.Common()
        object NavigateToDictionaryTranslation : NavigationCommand.Common()
        object NavigateToDictionaryManager : NavigationCommand.Common()
        object DoNothing : NavigationCommand.Common()
    }
    sealed class Platform: NavigationCommand() {
        object NavigateToDbBrowser : NavigationCommand.Platform()
    }
}

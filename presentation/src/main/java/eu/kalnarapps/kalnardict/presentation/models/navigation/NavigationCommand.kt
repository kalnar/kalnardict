package eu.kalnarapps.kalnardict.presentation.models.navigation

sealed class NavigationCommand {
    object Back : NavigationCommand()
    class NavigateToDictionaryRegistry(val uri: String) : NavigationCommand()
    class NavigateToDictionaryRegistryDialog(val uri: String) : NavigationCommand()
    object NavigateToDictionaryRegistryNewLanguageDialog : NavigationCommand()
    object NavigateToDictionaryQuery : NavigationCommand()
    object NavigateToDictionaryTranslation : NavigationCommand()
    object NavigateToDictionaryManager : NavigationCommand()
    object DoNothing : NavigationCommand()
}

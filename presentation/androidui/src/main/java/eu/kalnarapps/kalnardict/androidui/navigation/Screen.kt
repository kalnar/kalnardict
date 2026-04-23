package eu.kalnarapps.kalnardict.androidui.navigation

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    data object DictionaryQuery : Screen("main")
    data object DictionaryTranslation : Screen("dictionary_translation")
    data object PermissionError : Screen("permission_error")
    data object DictionaryManager : Screen("dictionary_manager")

    @Serializable
    data class DictionaryRegistry(val dbPath: String)
}



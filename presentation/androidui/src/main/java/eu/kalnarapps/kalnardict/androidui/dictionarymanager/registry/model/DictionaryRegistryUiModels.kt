package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model

data class DictionaryRegistryState(
    val dbPath: String,
    val tableInfoUiModels: List<ExternalTableUiInfo>,
    val errorMessages: List<String> = emptyList()
)

data class ExternalTableUiInfo(
    val originalTableName: String,
    val dictionaryName: String = originalTableName,
    val originalLanguageFrom: String,
    val languageFromUi: SelectableLanguage = SelectableLanguage.NotSet,
    val originalLanguageTo: String,
    val languageToUi: SelectableLanguage = SelectableLanguage.NotSet,
    val isSelected: Boolean = false
)

sealed class SelectableLanguage {
    data class LanguageUi(
        val name: String,
        val code: String
    ) : SelectableLanguage()

    object NotSet : SelectableLanguage()
}

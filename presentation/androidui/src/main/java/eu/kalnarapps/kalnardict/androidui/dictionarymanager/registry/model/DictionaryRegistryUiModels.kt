package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model

import eu.kalnarapps.kalnardict.androidui.common.model.UiNotification
import eu.kalnarapps.kalnardict.common.operations.OperationResult

data class DictionaryRegistryState(
    val dbPath: String,
    val tableInfoUiModels: List<ExternalTableUiInfo>,
    val availableLanguages: List<SelectableLanguage.LanguageUi> = emptyList(),
    val importResults: List<ImportTableResult> = emptyList(),
    val languageUpdated: UiNotification
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
    object AddNewLanguage : SelectableLanguage()
}

data class ImportTableResult(
    val originalName: String,
    val registeringName: String,
    val result: OperationResult
)

sealed class RegistryError() {
    object LanguageIdDuplicate : RegistryError()
}
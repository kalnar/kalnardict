package eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent

data class DictionaryRegistryState(
    val dbPath: String,
    val registerDictionaryUiModels: LoadableContent<List<RegisterDictionaryUi>>,
    val availableLanguages: List<SelectableLanguage.LanguageUi> = emptyList(),
    val importResults: List<ImportTableResult> = emptyList(),
    val importProgress: Map<ExternalTableUiInfo, DataOperationResult<ImportTableProgress>> = emptyMap()
)

data class ExternalTableUiInfo(
    val originalTableName: String,
    val dictionaryName: String = originalTableName,
    val dictionaryNameCursorIndexStart: Int = originalTableName.length,
    val dictionaryNameCursorIndexEnd: Int = originalTableName.length,
    val originalLanguageFrom: String,
    val languageFromUi: SelectableLanguage = SelectableLanguage.NotSet,
    val originalLanguageTo: String,
    val languageToUi: SelectableLanguage = SelectableLanguage.NotSet,
    val isSelected: Boolean = false
)

data class NewDictionaryInfoUi(
    val dbPath: String,
    val originalTableName: String,
    val dictionaryName: String = originalTableName,
    val originalLanguageFrom: String,
    val languageFromUi: SelectableLanguage.LanguageUi,
    val originalLanguageTo: String,
    val languageToUi: SelectableLanguage.LanguageUi,
    val isSelected: Boolean = false
)

data class RegisterDictionaryUi(
    val tableUiInfo: ExternalTableUiInfo,
    val availableLanguages: List<SelectableLanguage.LanguageUi>
)

sealed class SelectableLanguage {
    data class LanguageUi(
        val name: String,
        val code: String
    ) : SelectableLanguage()

    object NotSet : SelectableLanguage()
}

data class ImportTableResult(
    val originalName: String,
    val registeringName: String,
    val result: OperationResult
)

data class ImportTableProgress(
    val totalRows: Int,
    val registeredRows: Int
)

data class ImportTableStatus(
    val table: ExternalTableUiInfo,
    val progress: DataOperationResult<ImportTableProgress>
)

sealed class RegistryError() {
    object LanguageIdDuplicate : RegistryError()
}
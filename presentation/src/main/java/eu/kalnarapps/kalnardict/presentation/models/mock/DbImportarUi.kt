package eu.kalnarapps.kalnardict.presentation.models.mock

import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

data class DbImporterUi(
    val availableDatabases: List<String> = emptyList(),
    val availableLanguages: List<SelectableLanguage.LanguageUi> = emptyList(),
    val formData: ImporterFormData = ImporterFormData(),
    val showLoader: Boolean = false
)

data class ImporterFormData(
    val databaseName: String = "",
    val tableName: String = "",
    val sourceLanguage: String = "",
    val destinationLanguage: String = ""
)


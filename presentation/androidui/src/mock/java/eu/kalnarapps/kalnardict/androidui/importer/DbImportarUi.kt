package eu.kalnarapps.kalnardict.androidui.importer

import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage


data class DbImporterUi(
    val availableDatabases: List<String> = emptyList(),
    val availableLanguages: List<SelectableLanguage.LanguageUi> = emptyList(),
    val mockDatabasePath: String = "",
    val formData: ImporterFormData = ImporterFormData()
)

data class ImporterFormData(
    val databaseName: String = "",
    val tableName: String = "",
    val sourceLanguage: String = "",
    val destinationLanguage: String = ""
)


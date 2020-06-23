package eu.kalnarapps.kalnardict.androidui.dictionarymanager.model


data class ExternalTableUiInfo(
    val dictionaryName: String,
    val tableName: String,
    val languageFrom: String,
    val languageTo: String,
    val dbPath: String
)
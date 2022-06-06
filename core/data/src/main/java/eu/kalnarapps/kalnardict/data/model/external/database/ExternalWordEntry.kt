package eu.kalnarapps.kalnardict.data.model.external.database

data class ExternalTranslationEntry(
    val id: Int,
    val baseForm: String,
    val alternativeBaseForm: String = baseForm,
    val translation: String
)

data class ExternalTranslationCreation(
    val dbPath: String,
    val tableName: String,
    val externalTranslationEntries: List<ExternalTranslationEntry>
)

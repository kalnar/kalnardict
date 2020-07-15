package eu.kalnarapps.kalnardict.domain.entities.externaldatabase


data class ExternalDatabaseTable(
    val name: String,
    val languageFrom: String,
    val languageTo: String
)

data class ImportBatch(
    val table: ExternalDatabaseTable,
    val resource: ExternalDatabase,
    val displayName: String = table.name,
    val batchSize: Int
)

sealed class ExternalDatabase(val uri: String) {
    class LocalFile(val localPath: String) : ExternalDatabase(localPath)
}

data class ImportProgress(
    val totalRowCount: Int,
    val registeredCount: Int
)
package eu.kalnarapps.kalnardict.domain.entities.externaldatabase

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType


data class ExternalDatabaseTable(
    val name: String,
    val languageFrom: String,
    val languageTo: String
)

data class ImportJob(
    val table: ExternalDatabaseTable,
    val resource: ExternalDatabase,
    val displayName: String = table.name,
    val batchSize: Int,
    val displayTypes: List<DictionaryDisplayType> = listOf(
        DictionaryDisplayType.HTML,
        DictionaryDisplayType.TEXT
    )
)

sealed class ExternalDatabase(val uri: String) {
    data class LocalFile(val localPath: String) : ExternalDatabase(localPath)
}

data class ImportProgress(
    val dictionaryId: Int,
    val totalRowCount: Int,
    val registeredCount: Int
)
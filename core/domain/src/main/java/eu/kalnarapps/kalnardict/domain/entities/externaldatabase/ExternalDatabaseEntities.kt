package eu.kalnarapps.kalnardict.domain.entities.externaldatabase

import java.net.URI


data class ExternalDatabaseTable(
    val name: String,
    val languageFrom: String,
    val languageTo: String
)

data class ImportJob(
    val table: ExternalDatabaseTable,
    val resource: ExternalDatabase,
    val displayName: String = table.name
)

sealed class ExternalDatabase(open val uri: URI) {
    class LocalFile(override val uri: URI) : ExternalDatabase(uri)
    class RemoteFile(override val uri: URI) : ExternalDatabase(uri)
}
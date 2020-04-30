package eu.kalnarapps.kalnardict.domain.entities.externaldatabase

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import java.net.URI


data class ExternalDatabaseTable(
    val name: String,
    val languageFrom: DictLanguage,
    val languageTo: DictLanguage
)

data class ImportJob(
    val tables: List<ExternalDatabaseTable>,
    val resource: ExternalDatabase
)

sealed class ExternalDatabase(open val uri: URI) {
    class LocalFile(override val uri: URI) : ExternalDatabase(uri)
    class RemoteFile(override val uri: URI) : ExternalDatabase(uri)
}
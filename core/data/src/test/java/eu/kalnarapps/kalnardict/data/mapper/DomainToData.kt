package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable


fun ExternalDatabaseTable.toTableInfo(): ImportEntry.TableInfo {
    return object : ImportEntry.TableInfo {
        override fun name(): String = name
        override fun languageFrom(): String = languageFrom
        override fun languageTo(): String = languageTo
    }
}
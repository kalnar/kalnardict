package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.model.TableInfo
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable

fun ExternalDatabaseTable.toTableInfo(): ImportEntry.TableInfo {
    return TableInfo(
        name = name,
        languageFrom = languageFrom,
        languageTo = languageTo
    )
}
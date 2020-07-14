package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable

fun LanguageLogEntryData.toDictLanguage(): DictLanguage {
    return DictLanguage(
        name = this.name,
        code = this.id
    )
}

fun ImportEntry.TableInfo.toExternalDatabaseTable(): ExternalDatabaseTable {
    return ExternalDatabaseTable(
        name = name,
        languageFrom = languageFrom,
        languageTo = languageTo
    )
}

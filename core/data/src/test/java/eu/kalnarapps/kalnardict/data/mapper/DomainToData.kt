package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable


fun ExternalDatabaseTable.toTableInfo(): ImportEntry.TableInfo {
    return object : ImportEntry.TableInfo {
        override fun name(): String = name
        override fun languageFrom(): String = languageFrom
        override fun languageTo(): String = languageTo
    }
}

fun Dictionary.toDictionaryLogEntryData(): DictionaryLogEntryData {
    return TestDictionaryInfo(
        id = this.id,
        name = this.description,
        languageFrom = this.languageFrom.code,
        languageTo = this.languageTo.code
    )
}

data class TestDictionaryInfo(
    override val id: Int,
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : DictionaryLogEntryData


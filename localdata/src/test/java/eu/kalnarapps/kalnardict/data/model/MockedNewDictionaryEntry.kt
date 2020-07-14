package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData

data class MockedNewDictionaryEntry(
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : NewDictionaryLogEntryData

fun DictionaryLogEntry.toNewDictionaryEntry(): NewDictionaryLogEntryData {
    return MockedNewDictionaryEntry(
        name = this.dictionaryName,
        languageFrom = this.languageFrom,
        languageTo = this.languageTo
    )
}
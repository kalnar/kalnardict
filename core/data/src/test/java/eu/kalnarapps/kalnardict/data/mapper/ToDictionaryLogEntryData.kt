package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.mock.model.NewLogEntry

fun NewDictionaryLogEntryData.toDictionaryLogEntryData(id: Int): DictionaryLogEntryData {
    return NewLogEntry(
        id = id,
        name = this.name,
        languageFrom = this.languageFrom,
        languageTo = this.languageTo
    )
}
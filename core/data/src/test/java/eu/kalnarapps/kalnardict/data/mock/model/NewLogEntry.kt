package eu.kalnarapps.kalnardict.data.mock.model

import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData

data class NewLogEntry(
    override val id: Int,
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : DictionaryLogEntryData
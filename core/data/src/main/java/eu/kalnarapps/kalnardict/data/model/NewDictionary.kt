package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData

data class NewDictionary(
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : NewDictionaryLogEntryData
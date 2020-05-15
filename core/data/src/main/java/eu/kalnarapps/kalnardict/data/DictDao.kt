package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface DictDao {
    suspend fun insertDictEntry(dictEntry: DictEntry)
    suspend fun insertDictEntries(dictEntries: List<DictEntry>)
    suspend fun getDictEntryByQuery(query: String): List<DictEntry>
    suspend fun getDictionaries(): List<DictionaryLogEntryData>
}
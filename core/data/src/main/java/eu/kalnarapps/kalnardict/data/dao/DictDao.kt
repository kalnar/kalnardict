package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData

interface DictDao {
    suspend fun insertDictEntry(dictEntry: DictEntry)
    suspend fun insertDictEntries(dictEntries: List<DictEntry>)
    suspend fun getDictEntryByQuery(query: String): List<DictEntry>
    suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData)
    suspend fun getDictionaries(): List<DictionaryLogEntryData>
    suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData>
}
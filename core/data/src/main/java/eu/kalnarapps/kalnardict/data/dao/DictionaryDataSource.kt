package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData

interface DictionaryDataSource {
    suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long
    suspend fun getDictionaries(): List<DictionaryLogEntryData>
    suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData>
}
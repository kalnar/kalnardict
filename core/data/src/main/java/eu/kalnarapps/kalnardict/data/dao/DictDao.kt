package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry

interface DictDao {
    suspend fun insertDictEntry(wordDataEntry: TranslatedWordDataEntry)
    suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordDataEntry>)
    suspend fun queryWithMatchAnyWhere(query: String): List<WordDataEntry>
    suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData)
    suspend fun getDictionaries(): List<DictionaryLogEntryData>
    suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData>
    suspend fun getTranslationByWordAndDictionaryId(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String>
}
package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry

interface DictDao {
    suspend fun insertDictEntry(wordDataEntry: TranslatedWordInsertEntry)
    suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordInsertEntry>)
    suspend fun queryWithMatchAnyWhere(query: String): List<WordDataEntry>
    suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long
    suspend fun getDictionaries(): List<DictionaryLogEntryData>
    suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData>
    suspend fun getTranslationByWordAndDictionaryId(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String>
}
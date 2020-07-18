package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry

interface WordDataSource {
    suspend fun insertDictEntry(wordDataEntry: TranslatedWordInsertEntry)
    suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordInsertEntry>): OperationResult
    suspend fun queryWithMatchAnyWhereInDictionary(
        query: String,
        dictionaryId: Int
    ): List<WordDataEntry>

    suspend fun getTranslationByWordAndDictionaryId(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String>
}
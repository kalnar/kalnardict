package eu.kalnarapps.kalnardict.data.datasources.dictionary

import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import kotlinx.coroutines.flow.Flow

interface DictionaryDisplayTypeDataSource {
    fun displayTypeForDictionaryById(dictionaryId: Int): Flow<DictionaryDisplayTypeDataEntry>
}
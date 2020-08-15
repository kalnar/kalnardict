package eu.kalnarapps.kalnardict.data.datasources.dictionary

import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import kotlinx.coroutines.flow.Flow

interface DictionaryDisplayTypeDataSource {
    fun displayTypeForDictionaryById(dictionaryId: Int): DictionaryDisplayTypeDataEntry
    fun displayTypeForDictionaryByIdFlow(dictionaryId: Int): Flow<DictionaryDisplayTypeDataEntry>
    fun supportedDisplayTypesForDictionaryById(dictionaryId: Int): Flow<List<DictionaryDisplayTypeDataEntry>>
    suspend fun setDisplayTypeForDictionaryById(
        dictionaryId: Int,
        displayTypeData: DictionaryDisplayTypeDataEntry
    )
    suspend fun addSupportedDisplayTypeForDictionaryById(
        dictionaryId: Int,
        displayTypesData: List<DictionaryDisplayTypeDataEntry>
    )
}
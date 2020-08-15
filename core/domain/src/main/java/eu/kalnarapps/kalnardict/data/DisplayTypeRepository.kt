package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.flow.Flow

interface DisplayTypeRepository {
    fun getDisplayTypeFlowFor(dictionary: Dictionary): Flow<DictionaryDisplayType>
    fun getDisplayTypeFor(dictionary: Dictionary): DictionaryDisplayType
    suspend fun setDisplayTypeFor(
        dictionaryId: Int,
        displayType: DictionaryDisplayType
    )

    suspend fun addDisplayTypesFor(
        dictionaryId: Int,
        displayTypes: List<DictionaryDisplayType>
    )

    fun getSupportedDisplayTypesFor(dictionary: Dictionary): Flow<List<DictionaryDisplayType>>
}
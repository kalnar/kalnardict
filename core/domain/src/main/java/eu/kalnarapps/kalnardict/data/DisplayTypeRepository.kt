package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.flow.Flow

interface DisplayTypeRepository {
    fun getDisplayTypeFor(dictionary: Dictionary): Flow<DictionaryDisplayType>
    suspend fun setDisplayTypeFor(
        dictionaryId: Int,
        displayType: DictionaryDisplayType
    )

    fun getSupportedDisplayTypesFor(dictionary: Dictionary): Flow<List<DictionaryDisplayType>>
}
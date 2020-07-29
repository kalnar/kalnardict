package eu.kalnarapps.kalnardict.data.dao.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import kotlinx.coroutines.flow.Flow

interface DisplayTypePreferencesDao {
    fun getDisplayTypeForDictionary(dictionaryId: Int): String
    fun getDisplayTypeForDictionaryFlow(dictionaryId: Int): Flow<String>
    suspend fun setDisplayTypeForDictionary(dictionaryId: Int, type: String)
}

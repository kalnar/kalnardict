package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    suspend fun getCurrentDictionary(): CurrentDictionary
    suspend fun getCurrentAccentMode(): AccentMode
    suspend fun updateCurrentDictionary(dictionary: Dictionary)
}

interface QueryModeConfigurationRepository {
    fun getCurrentQueryMode(): Flow<QueryMode>
    suspend fun updateCurrentQueryMode(queryMode: QueryMode)
}

sealed class CurrentDictionary {
    class SetDictionary(val dictionary: Dictionary) : CurrentDictionary()
    object DictionaryNotSet : CurrentDictionary()
}
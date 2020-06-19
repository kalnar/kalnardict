package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface ConfigurationRepository {
    suspend fun getCurrentDictionary(): CurrentDictionary
    suspend fun getCurrentAccentMode(): AccentMode
    suspend fun updateCurrentDictionary(dictionary: Dictionary)
}

sealed class CurrentDictionary {
    class SetDictionary(val dictionary: Dictionary) : CurrentDictionary()
    object DictionaryNotSet : CurrentDictionary()
}
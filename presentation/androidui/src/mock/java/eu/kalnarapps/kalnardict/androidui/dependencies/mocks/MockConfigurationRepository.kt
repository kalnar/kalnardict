package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

class MockConfigurationRepository : ConfigurationRepository {
    private var currentDictionary: CurrentDictionary = CurrentDictionary.DictionaryNotSet
    override suspend fun getCurrentDictionary(): CurrentDictionary =
        currentDictionary

    override suspend fun getCurrentAccentMode(): AccentMode =
        AccentMode.ACCENT_SENSITIVE

    override suspend fun updateCurrentDictionary(dictionary: Dictionary) {
        currentDictionary = CurrentDictionary.SetDictionary(dictionary)
    }
}
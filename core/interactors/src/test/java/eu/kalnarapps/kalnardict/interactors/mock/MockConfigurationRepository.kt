package eu.kalnarapps.kalnardict.interactors.mock

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

class MockConfigurationRepository(
    private var currentDictionary: CurrentDictionary
) : ConfigurationRepository {

    override fun getCurrentDictionary(): CurrentDictionary =
        currentDictionary

    override suspend fun getCurrentAccentMode(): AccentMode =
        AccentMode.ACCENT_SENSITIVE

    override suspend fun updateCurrentDictionary(dictionary: Dictionary) {
        currentDictionary = CurrentDictionary.SetDictionary(dictionary)
    }
}

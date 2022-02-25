package eu.kalnarapps.kalnardict.interactors.mock

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockConfigurationRepository(
    private var currentDictionary: CurrentDictionary
) : ConfigurationRepository {

    override fun getCurrentDictionary(): Flow<CurrentDictionary> =
        flowOf(currentDictionary)

    override suspend fun getCurrentAccentMode(): AccentMode =
        AccentMode.ACCENT_SENSITIVE

    override suspend fun updateCurrentDictionary(dictionary: Dictionary) {
        currentDictionary = CurrentDictionary.SetDictionary(dictionary)
    }
}

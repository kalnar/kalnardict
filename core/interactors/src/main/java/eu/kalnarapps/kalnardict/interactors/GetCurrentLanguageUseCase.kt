package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import kotlinx.coroutines.flow.Flow

class GetCurrentLanguageUseCase(
    private val configurationRepository: ConfigurationRepository
) : GetLanguageUseCase {
    override fun invoke(): Flow<CurrentDictionary> {
        return configurationRepository.getCurrentDictionary()
    }
}
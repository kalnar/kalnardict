package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase

class GetCurrentLanguageUseCase(
    private val configurationRepository: ConfigurationRepository
) : GetLanguageUseCase {
    override suspend fun invoke(): CurrentDictionary {
        return configurationRepository.getCurrentDictionary()
    }
}
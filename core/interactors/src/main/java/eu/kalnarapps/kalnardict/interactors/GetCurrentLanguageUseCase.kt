package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase

class GetCurrentLanguageUseCase(
    private val configurationRepository: ConfigurationRepository,
    private val dictionaryRepository: DictionaryRepository
) : GetLanguageUseCase {
    override suspend fun invoke(): Dictionary {
        return when (
            val dictionaryResult = configurationRepository.getCurrentDictionary()
            ) {
            is CurrentDictionary.SetDictionary -> dictionaryResult.dictionary
            CurrentDictionary.DictionaryNotSet -> {
                dictionaryRepository.readRegisteredDictionaries().first()
            }
        }
    }
}
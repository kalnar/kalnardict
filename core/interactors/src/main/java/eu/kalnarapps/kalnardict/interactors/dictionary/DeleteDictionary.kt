package eu.kalnarapps.kalnardict.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.usecases.DeleteDictionaryUseCase

class DeleteDictionary(
    private val dictionaryRepository: DictionaryRepository,
    private val configurationRepository: ConfigurationRepository
) : DeleteDictionaryUseCase {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        return when (val result = dictionaryRepository.deleteDictionaryById(dictionaryId)) {
            is OperationResult.Failure -> result
            OperationResult.Success -> {
                when (
                    val currentDictionary = configurationRepository.getCurrentDictionaryOneShot()
                ) {
                    CurrentDictionary.DictionaryNotSet -> {
                        configurationRepository.updateCurrentDictionaryWithFirst()
                    }
                    is CurrentDictionary.SetDictionary -> {
                        if (currentDictionary.dictionary.id == dictionaryId) {
                            configurationRepository.updateCurrentDictionaryWithFirst()
                        }
                    }
                }
                result
            }
        }
    }
}
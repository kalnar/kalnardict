package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase


class UpdateCurrentLanguage(
    private val dictionaryRepository: DictionaryRepository,
    private val configurationRepository: ConfigurationRepository
) : ChangeDictLanguageUseCase {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        return when (
            val dictionaryResult = dictionaryRepository.getDictionaryById(dictionaryId)
            ) {
            is DataOperationResult.Success -> {
                configurationRepository.updateCurrentDictionary(
                    dictionaryResult.data
                )
                OperationResult.Success
            }
            is DataOperationResult.Failure -> {
                OperationResult.Failure(
                    errorMessage = "dictionary could not be read by id: $dictionaryId",
                    cause = dictionaryResult
                )
            }
        }
    }

}
package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetTranslation(
    private val configurationRepository: ConfigurationRepository,
    private val repository: DictionaryRepository
) : GetTranslationUseCase {
    override suspend fun invoke(wordId: Int): DataOperationResult<String> {
        return configurationRepository.getCurrentDictionary().map {
            when (val dictionary = it) {
                is CurrentDictionary.SetDictionary -> {
                    repository.getTranslationById(
                        wordId = wordId,
                        dictionaryId = dictionary.dictionary.id
                    )
                }
                CurrentDictionary.DictionaryNotSet -> {
                    DataOperationResult.Failure(
                        errorMessage = "incorrect use of use case, there is no dictionary set at " +
                                "the time of invoking getting the translation"
                    )
                }
            }
        }.first()
    }
}
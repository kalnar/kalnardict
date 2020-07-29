package eu.kalnarapps.kalnardict.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetDictionaryByIdUseCase

class GetDictionaryById(
    private val dictionaryRepository: DictionaryRepository
) : GetDictionaryByIdUseCase {
    override suspend fun invoke(id: Int): DataOperationResult<Dictionary> {
        return dictionaryRepository.getDictionaryById(id)
    }
}
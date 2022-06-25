package eu.kalnarapps.kalnardict.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.usecases.DeleteDictionaryUseCase

class DeleteDictionary(
    private val dictionaryRepository: DictionaryRepository
) : DeleteDictionaryUseCase {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        return dictionaryRepository.deleteDictionaryById(dictionaryId)
    }
}
package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.DeleteDictionaryUseCase

class DeleteDictionaryFromUi(
    private val deleteDictionaryUseCase: DeleteDictionaryUseCase
): DeleteDictionaryUseCaseFromUi {
    override suspend operator fun invoke(dictionaryId: Int): OperationResult {
        return deleteDictionaryUseCase.invoke(dictionaryId)
    }
}
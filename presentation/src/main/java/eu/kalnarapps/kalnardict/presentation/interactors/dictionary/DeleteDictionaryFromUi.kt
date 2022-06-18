package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult

class DeleteDictionaryFromUi : DeleteDictionaryUseCaseFromUi {
    override suspend operator fun invoke(dictionaryId: Int): OperationResult {
        return OperationResult.Success
    }
}
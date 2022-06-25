package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult

interface DeleteDictionaryUseCaseFromUi {
    suspend operator fun invoke(dictionaryId: Int): OperationResult
}
package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.OperationResult

interface DeleteDictionaryUseCase {
    suspend fun invoke(dictionaryId: Int): OperationResult
}
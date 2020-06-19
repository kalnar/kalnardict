package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.OperationResult

interface ChangeDictLanguageUseCase {
    suspend operator fun invoke(dictionaryId: Int): OperationResult
}
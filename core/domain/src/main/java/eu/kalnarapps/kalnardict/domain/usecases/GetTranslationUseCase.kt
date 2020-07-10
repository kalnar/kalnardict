package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

interface GetTranslationUseCase {
    suspend operator fun invoke(wordId: Int): DataOperationResult<String>
}
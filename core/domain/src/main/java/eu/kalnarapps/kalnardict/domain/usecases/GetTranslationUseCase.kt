package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import kotlinx.coroutines.flow.Flow

interface GetTranslationUseCase {
    suspend operator fun invoke(wordId: Int): DataOperationResult<String>
}
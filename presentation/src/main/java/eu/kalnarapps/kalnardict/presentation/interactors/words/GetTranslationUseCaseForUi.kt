package eu.kalnarapps.kalnardict.presentation.interactors.words

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import kotlinx.coroutines.flow.Flow

interface GetTranslationUseCaseForUi {
    suspend operator fun invoke(wordId: Int): DataOperationResult<String>
}
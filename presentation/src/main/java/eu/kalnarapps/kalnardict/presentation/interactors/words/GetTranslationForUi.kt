package eu.kalnarapps.kalnardict.presentation.interactors.words

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase

class GetTranslationForUi(
    private val getTranslationUseCase: GetTranslationUseCase
) : GetTranslationUseCaseForUi {
    override suspend operator fun invoke(wordId: Int): DataOperationResult<String> {
        return getTranslationUseCase(wordId)
    }
}
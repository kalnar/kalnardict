package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase

class ChangeDictionaryFromUi(
    private val changeDictLanguageUseCase: ChangeDictLanguageUseCase
) : ChangeDictionaryUseCaseFromUi {
    override suspend operator fun invoke(dictionaryId: Int): OperationResult {
        return changeDictLanguageUseCase(dictionaryId)
    }
}
package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase

class MockLoadTranslationUseCase : GetTranslationUseCase {
    override suspend fun invoke(wordId: Int): DataOperationResult<String> {
        return DataOperationResult.Success("translation for the word with id: $wordId")
    }
}
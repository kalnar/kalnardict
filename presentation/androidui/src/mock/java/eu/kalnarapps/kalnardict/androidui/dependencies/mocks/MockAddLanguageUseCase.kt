package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase

class MockAddLanguageUseCase : RegisterLanguageUseCase {
    override suspend fun invoke(language: DictLanguage): OperationResult {
        return OperationResult.Failure(
            Stubs.Errors.duplicateLangugageId
        )
    }
}
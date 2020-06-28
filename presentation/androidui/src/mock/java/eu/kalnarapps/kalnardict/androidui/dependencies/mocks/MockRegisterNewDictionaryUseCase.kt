package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase

class MockRegisterNewDictionaryUseCase : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult = OperationResult.Success
}
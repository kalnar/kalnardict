package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase

class MockRegisterNewDictionaryUseCase() : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult =
        if (dbUri == UiStubs.Uris.validUri) OperationResult.Success else OperationResult.Failure(
            errorMessage = "this mock fails always"
        )
}
package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockRegisterNewDictionaryUseCase() : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> = flow {
        if (dbUri == UiStubs.Uris.validUri) {
            val total = 160
            var progressIndicator = 0
            while (progressIndicator < total) {
                progressIndicator += 9
                emit(
                    DataOperationResult.Success(
                        ImportProgress(
                            0,
                            total,
                            progressIndicator,
                        )
                    )
                )
                delay(250L)
            }
            if (total != progressIndicator) {
                emit(
                    DataOperationResult.Success(
                        ImportProgress(
                            0,
                            total,
                            total
                        )
                    )
                )
            }
        } else {
            emit(
                DataOperationResult.Failure(
                    errorMessage = "this mock fails always"
                )
            )
        }
    }
}
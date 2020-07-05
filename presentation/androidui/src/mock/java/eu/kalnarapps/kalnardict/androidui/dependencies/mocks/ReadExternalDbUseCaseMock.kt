package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase

class ReadExternalDbUseCaseMock(private val isAlwaysValid: Boolean = false) :
    ReadExternalDbUseCase {
    override suspend fun invoke(uri: String): DataOperationResult<List<ExternalDatabaseTable>> {
        return when {
            uri == UiStubs.Uris.validUri || isAlwaysValid -> DataOperationResult.Success(
                listOf(
                    UiStubs.TableDbInfo.externalTableInfo1,
                    UiStubs.TableDbInfo.externalTableInfo2
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "db: $uri is not of correct format"
            )
        }
    }
}
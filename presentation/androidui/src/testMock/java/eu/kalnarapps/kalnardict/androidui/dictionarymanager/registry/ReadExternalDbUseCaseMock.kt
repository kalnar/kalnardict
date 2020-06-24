package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import eu.kalnarapps.kalnardict.androidui.UiStubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import java.net.URI

class ReadExternalDbUseCaseMock : ReadExternalDbUseCase {
    override suspend fun invoke(uri: URI): DataOperationResult<List<ExternalDatabaseTable>> {
        return when (uri) {
            UiStubs.Uris.validUri -> DataOperationResult.Success(
                listOf(
                    UiStubs.TableDbInfo.externalTableInfo1,
                    UiStubs.TableDbInfo.externalTableInfo2
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "db: ${uri.path} is not of correct format"
            )
        }
    }
}
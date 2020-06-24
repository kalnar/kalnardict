package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import java.net.URI

interface ReadExternalDbUseCase {
    suspend operator fun invoke(uri: URI): DataOperationResult<List<ExternalDatabaseTable>>
}
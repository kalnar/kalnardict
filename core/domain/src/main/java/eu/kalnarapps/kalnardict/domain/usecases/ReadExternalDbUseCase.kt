package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable

interface ReadExternalDbUseCase {
    suspend operator fun invoke(uri: String): DataOperationResult<List<ExternalDatabaseTable>>
}
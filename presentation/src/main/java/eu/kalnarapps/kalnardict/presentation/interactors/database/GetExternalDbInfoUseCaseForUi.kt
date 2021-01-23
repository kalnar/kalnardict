package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo

interface GetExternalDbInfoUseCaseForUi {
    suspend operator fun invoke(uri: String): DataOperationResult<List<ExternalTableUiInfo>>
}
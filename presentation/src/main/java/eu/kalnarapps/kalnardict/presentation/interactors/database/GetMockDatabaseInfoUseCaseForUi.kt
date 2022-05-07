package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData

interface GetMockDatabaseInfoUseCaseForUi {
    suspend operator fun invoke(): DataOperationResult<String>
}
package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData

interface CreateMockTableUseCaseFromUi  {
    suspend operator fun invoke(importerFormData: ImporterFormData): DataOperationResult<String>
}
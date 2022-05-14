package eu.kalnarapps.kalnardict.domain.usecases.mockdatabase

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo

interface CreateExternalTableUseCase {
    suspend fun invoke(jobInfo: ExternalTableCreationJobInfo): OperationResult
}
package eu.kalnarapps.kalnardict.domain.usecases.mockdatabase

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.MockDatabaseInfo

interface GetMockDatabaseSettingsUseCase {
    suspend fun invoke(): DataOperationResult<MockDatabaseInfo>
}
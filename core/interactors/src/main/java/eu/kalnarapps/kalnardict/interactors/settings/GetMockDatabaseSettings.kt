package eu.kalnarapps.kalnardict.interactors.settings

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.GetMockDatabaseSettingsRepository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.MockDatabaseInfo
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.GetMockDatabaseSettingsUseCase

class GetMockDatabaseSettings(
    private val mockDatabaseSettingsRepository: GetMockDatabaseSettingsRepository
) : GetMockDatabaseSettingsUseCase {
    override suspend fun invoke(): DataOperationResult<MockDatabaseInfo> {
        return DataOperationResult.Success(
            MockDatabaseInfo(
                directoryPath = mockDatabaseSettingsRepository.getMockDatabaseDirectoryPath()
            )
        )
    }
}
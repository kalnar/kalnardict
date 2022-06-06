package eu.kalnarapps.kalnardict.interactors.settings

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.MockDatabaseInfo
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.GetMockDatabaseSettingsUseCase

class GetMockDatabaseSettings : GetMockDatabaseSettingsUseCase {
    override suspend fun invoke(): DataOperationResult<MockDatabaseInfo> {
        return DataOperationResult.Success(
            MockDatabaseInfo(
                "/storage/emulated/0/Download/.kalnardict/mock"
            )
        )
    }
}
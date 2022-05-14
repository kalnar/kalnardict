package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.CreateExternalTableUseCase
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.GetMockDatabaseSettingsUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainConverter
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData

class CreateMockTableFromUi(
    private val getMockDatabaseInfo: GetMockDatabaseSettingsUseCase,
    private val createExternalTableUseCase: CreateExternalTableUseCase,
    private val uiToDomainConverter: UiToDomainConverter<ImporterFormData, ExternalTableCreationJobInfo, String>
) : CreateMockTableUseCaseFromUi {
    override suspend operator fun invoke(
        importerFormData: ImporterFormData
    ): DataOperationResult<String> {
        return when (val getPath = getMockDatabaseInfo.invoke()) {
            is DataOperationResult.Failure -> DataOperationResult.Failure(
                errorMessage = "GetMockDatabaseSettingsUseCase failed",
                cause = getPath
            )
            is DataOperationResult.Success -> {
                val jobInfo = with(uiToDomainConverter) {
                    importerFormData.toDomain(getPath.data.directoryPath)
                }
                when (val createTable = createExternalTableUseCase.invoke(jobInfo)) {
                    is OperationResult.Failure -> DataOperationResult.Failure(
                        errorMessage = "CreateExternalTableUseCase failed",
                        cause = createTable
                    )
                    is OperationResult.Success -> DataOperationResult.Success(jobInfo.dbPath.localPath)
                }
            }
        }
    }
}
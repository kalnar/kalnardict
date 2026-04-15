package eu.kalnarapps.kalnardict.interactors.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DatabaseRepository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.CreateExternalTableUseCase

class CreateExternalTable(
    private val databaseRepository: DatabaseRepository,
    private val tableRepository: DatabaseRepository.TableRepository
) : CreateExternalTableUseCase {
    override suspend fun invoke(jobInfo: ExternalTableCreationJobInfo): OperationResult {
        println("invoke db creation: filepath: ${jobInfo.dbPath.localPath}")
        return when (
            val createDatabase = databaseRepository.createDatabase(jobInfo.dbPath.localPath)
        ) {
            is OperationResult.Failure -> OperationResult.Failure(
                errorMessage = "${DatabaseRepository::class.java.simpleName}::createDatabase failed with argument: ${jobInfo.dbPath.localPath}",
                cause = createDatabase
            )
            OperationResult.Success -> {
                when (
                    val createTable = tableRepository.createTable(jobInfo)
                ) {
                    is OperationResult.Failure -> OperationResult.Failure(
                        errorMessage = "${DatabaseRepository.TableRepository::class.java.simpleName}::createTable failed with argument: $jobInfo",
                        cause = createTable
                    )
                    OperationResult.Success -> OperationResult.Success
                }
            }
        }
    }
}

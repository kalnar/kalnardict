package eu.kalnarapps.kalnardict.data.repositories.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DatabaseRepository
import eu.kalnarapps.kalnardict.data.datasources.database.DatabaseMetaDataSource
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway

class DefaultDatabaseRepository(
    private val databaseMetaDataSource: DatabaseMetaDataSource,
    private val externalDatabaseGateway: ExternalDatabaseGateway
) : DatabaseRepository {
    override suspend fun createDatabase(path: String): OperationResult {
        return when (val createDb = externalDatabaseGateway.createDatabase(path)) {
            is OperationResult.Failure -> OperationResult.Failure(
                errorMessage = "${ExternalDatabaseGateway::class.java.simpleName}::createDatabase failed with $path",
                cause = createDb
            )
            OperationResult.Success -> {
                databaseMetaDataSource.addDatabase(path)
                OperationResult.Success
            }
        }
    }
}

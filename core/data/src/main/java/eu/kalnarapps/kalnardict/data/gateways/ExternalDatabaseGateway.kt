package eu.kalnarapps.kalnardict.data.gateways

import eu.kalnarapps.kalnardict.common.operations.OperationResult

interface ExternalDatabaseGateway {
    suspend fun createDatabase(path: String): OperationResult
}
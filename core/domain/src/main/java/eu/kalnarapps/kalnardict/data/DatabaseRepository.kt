package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo

interface DatabaseRepository {
    suspend fun createDatabase(path: String): OperationResult

    interface TableRepository {
        suspend fun createTable(tableCreationJobInfo: ExternalTableCreationJobInfo): OperationResult
    }
}

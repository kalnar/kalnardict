package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.operations.DataOperationResult

interface ExternalDatabaseHandler {

    fun checkDatabaseStructure(resource: ExternalDatabase): DatabaseValidity
    fun readTableFrom(importJob: ImportJob): DataOperationResult<List<DictEntry>>

}

enum class DatabaseValidity {
    VALID,
    INVALID;
}

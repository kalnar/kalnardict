package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.*


class ExternalDbImporter : ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity {
        return DatabaseValidity.VALID
    }

    override fun readTableFrom(importJob: ImportEntry): DataOperationResult<List<DictEntry>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
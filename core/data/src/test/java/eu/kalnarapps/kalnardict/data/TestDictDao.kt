package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.operations.DataOperationResult

class TestDictDao : DictDao {
    private val mockDb = ArrayList<DictEntry>()
    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        mockDb.add(dictEntry)
    }

    override suspend fun insertDictEntries(dictEntries: List<DictEntry>) {
        mockDb.addAll(dictEntries)
    }

    override suspend fun getDictEntryByQuery(query: DictQuery): List<DictEntry> {
        return mockDb.filter { it.getBaseForm().contains(query.queryString) }
    }
}

class TestExternalDatabaseHandler :
    ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDatabase): DatabaseValidity {
        return DatabaseValidity.INVALID
    }

    override fun readTableFrom(importJob: ImportJob): DataOperationResult<List<DictEntry>> {
        val resource = importJob.resource
        return if (resource == validExternalResource) {
            DataOperationResult.Success(data = newWords)
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

}
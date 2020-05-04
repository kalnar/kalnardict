package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

class TestDictDao : DictDao {
    private val mockDb = ArrayList<DictEntry>()
    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        mockDb.add(dictEntry)
    }

    override suspend fun insertDictEntries(dictEntries: List<DictEntry>) {
        mockDb.addAll(dictEntries)
    }

    override suspend fun getDictEntryByQuery(query: String): List<DictEntry> {
        return mockDb.filter { it.getBaseForm().contains(query) }
    }
}

class TestExternalDatabaseHandler :
    ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity {
        return DatabaseValidity.INVALID
    }

    override fun readTableFrom(importJob: ImportEntry): DataOperationResult<List<DictEntry>> {
        val resource = importJob.externalDictionaryResource().uri()
        return if (resource == validExternalResource.uri) {
            DataOperationResult.Success(data = newWords)
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

}
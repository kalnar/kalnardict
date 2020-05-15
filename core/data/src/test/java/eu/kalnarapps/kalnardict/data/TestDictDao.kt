package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

open class TestDictDao : DictDao {
    private val mockDb = ArrayList<DictEntry>()
    protected val mockDictionaries = ArrayList<DictionaryLogEntryData>().apply {
        add(
            object : DictionaryLogEntryData {
                override val id: Int
                    get() = 1
                override val name: String
                    get() = "test_fr_dictionary"
                override val languageFrom: String
                    get() = "hu"
                override val languageTo: String
                    get() = "fr"
            }
        )
    }

    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        mockDb.add(dictEntry)
    }

    override suspend fun insertDictEntries(dictEntries: List<DictEntry>) {
        mockDb.addAll(dictEntries)
    }

    override suspend fun getDictEntryByQuery(query: String): List<DictEntry> {
        return mockDb.filter { it.getBaseForm().contains(query) }
    }

    override suspend fun getDictionaries(): List<DictionaryLogEntryData> {
        return mockDictionaries
    }
}

class TestEmptyDictDao : TestDictDao() {
    init {
        mockDictionaries.clear()
    }
}

class TestExternalDatabaseHandler :
    ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity {
        return DatabaseValidity.INVALID
    }

    override fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>> {
        TODO("Not yet implemented")
    }

    override fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<DictEntry>> {
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
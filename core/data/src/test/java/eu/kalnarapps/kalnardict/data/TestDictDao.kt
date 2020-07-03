package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toTableInfo

open class TestDictDao : DictDao {
    private val mockDb = ArrayList<DictEntry>()
    protected val mockDictionaries = ArrayList<DictionaryLogEntryData>().apply {
        addAll(
            listOf(
                object :
                    DictionaryLogEntryData {
                    override val id: Int
                        get() = 1
                    override val name: String
                        get() = "test_fr_dictionary"
                    override val languageFrom: String
                        get() = "hu"
                    override val languageTo: String
                        get() = "fr"
                },
                Stubs.Dictionaries.frenchEnglishDictionary.toDictionaryLogEntryData()
            )
        )
    }

    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        mockDb.add(dictEntry)
    }

    override suspend fun insertDictEntries(dictEntries: List<DictEntry>) {
        mockDb.addAll(dictEntries)
    }

    override suspend fun getDictEntryByQuery(query: String): List<DictEntry> {
        return mockDb.filter { it.baseForm.contains(query) }
    }

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData) {
        mockDictionaries.add(
            newDictionary.toDictionaryLogEntryData(id = mockDictionaries.size + 1)
        )
    }

    override suspend fun getDictionaries(): List<DictionaryLogEntryData> {
        return mockDictionaries
    }

    override suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData> {
        return mockDictionaries.find { it.id == id }?.let {
            DataOperationResult.Success(
                data = it
            )
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionary found by the id: $id"
        )
    }
}

private fun NewDictionaryLogEntryData.toDictionaryLogEntryData(id: Int): DictionaryLogEntryData {
    return NewLogEntry(
        id = id,
        name = this.name,
        languageFrom = this.languageFrom,
        languageTo = this.languageTo
    )
}

data class NewLogEntry(
    override val id: Int,
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : DictionaryLogEntryData

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
        return when (resource.uri()) {
            Stubs.Db.validExternalDatabase.uri -> DataOperationResult.Success(
                listOf(
                    Stubs.MetaInfoOnDb.table1.toTableInfo(),
                    Stubs.MetaInfoOnDb.table2.toTableInfo()
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "error while reading ${resource.uri()}"
            )
        }
    }

    override fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<DictEntry>> {
        val resource = importJob.externalDictionaryResource().uri()
        return if (resource == validExternalResource.uri) {
            DataOperationResult.Success(data = newWordsInFrench)
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

}
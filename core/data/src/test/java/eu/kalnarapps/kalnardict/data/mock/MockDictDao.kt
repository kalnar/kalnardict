package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordImportEntry
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toTableInfo
import eu.kalnarapps.kalnardict.data.newWordsInFrench
import eu.kalnarapps.kalnardict.data.validExternalResource

open class MockDictDao(
    private val mockDb: ArrayList<TranslatedWordDataEntry> = ArrayList<TranslatedWordDataEntry>()
) : DictDao {
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

    override suspend fun insertDictEntry(wordDataEntry: TranslatedWordInsertEntry) {
        mockDb.add(
            MockTranslatedWordEntry(
                id = mockDb.size,
                baseForm = wordDataEntry.baseForm,
                alternativeBaseForm = wordDataEntry.alternativeBaseForm,
                translation = wordDataEntry.translation,
                dictionaryId = wordDataEntry.dictionaryId
            )
        )
    }

    override suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordInsertEntry>) {
        mockDb.addAll(
            wordDataEntries.map { wordDataEntry ->
                MockTranslatedWordEntry(
                    id = mockDb.size,
                    baseForm = wordDataEntry.baseForm,
                    alternativeBaseForm = wordDataEntry.alternativeBaseForm,
                    translation = wordDataEntry.translation,
                    dictionaryId = wordDataEntry.dictionaryId
                )

            }
        )
    }

    override suspend fun queryWithMatchAnyWhere(query: String): List<WordDataEntry> {
        return mockDb.filter { it.baseForm.contains(query) }
    }

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long {
        mockDictionaries.add(
            newDictionary.toDictionaryLogEntryData(id = mockDictionaries.size + 1)
        )
        return (mockDictionaries.size + 1).toLong()
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

    override suspend fun getTranslationByWordAndDictionaryId(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        return mockDb.find { it.dictionaryId == dictionaryId && it.id == wordId }?.let {
            DataOperationResult.Success(it.translation)
        } ?: DataOperationResult.Failure<String>(
            errorMessage = "(wordId: $wordId; dictionaryId: $dictionaryId) combination " +
                    "isn't correct, there is no such word in given dictionary"
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

data class MockTranslatedWordEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : TranslatedWordDataEntry

class MockEmptyDictDao : MockDictDao() {
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
        return when (resource.sdCardPath()) {
            Stubs.Db.validExternalDatabase.localPath -> DataOperationResult.Success(
                listOf(
                    Stubs.MetaInfoOnDb.table1.toTableInfo(),
                    Stubs.MetaInfoOnDb.table2.toTableInfo()
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "error while reading ${resource.sdCardPath()}"
            )
        }
    }

    override fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<TranslatedWordImportEntry>> {
        val resource = importJob.externalDictionaryResource().sdCardPath()
        return if (resource == validExternalResource.localPath) {
            DataOperationResult.Success(data = newWordsInFrench)
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

}
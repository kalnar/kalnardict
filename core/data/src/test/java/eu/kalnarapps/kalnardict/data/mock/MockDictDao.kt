package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mock.model.MockTranslatedWordEntry

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
                id = mockDb.size + 1,
                baseForm = wordDataEntry.baseForm,
                alternativeBaseForm = wordDataEntry.alternativeBaseForm,
                translation = wordDataEntry.translation,
                dictionaryId = wordDataEntry.dictionaryId
            )
        )
    }

    override suspend fun insertDictEntries(
        wordDataEntries: List<TranslatedWordInsertEntry>
    ): OperationResult {
        mockDb.addAll(
            wordDataEntries.map { wordDataEntry ->
                MockTranslatedWordEntry(
                    id = mockDb.size + 1,
                    baseForm = wordDataEntry.baseForm,
                    alternativeBaseForm = wordDataEntry.alternativeBaseForm,
                    translation = wordDataEntry.translation,
                    dictionaryId = wordDataEntry.dictionaryId
                )

            }
        )
        return OperationResult.Success
    }

    override suspend fun queryWithMatchAnyWhereInDictionary(
        query: String,
        dictionaryId: Int
    ): List<WordDataEntry> {
        return mockDb.filter { it.dictionaryId == dictionaryId && it.baseForm.contains(query) }
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

class MockEmptyDictDao : MockDictDao() {
    init {
        mockDictionaries.clear()
    }
}


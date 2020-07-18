package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData

open class MockDictionaryDataSource : DictionaryDataSource {
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

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long {
        val newId = mockDictionaries.size + 1
        mockDictionaries.add(
            newDictionary.toDictionaryLogEntryData(id = newId)
        )
        return (newId).toLong()
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

package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class MockDictionaryDataSource(
    private val mockDictionaries: ArrayList<DictionaryLogEntryData> = ArrayList<DictionaryLogEntryData>(
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
) : DictionaryDataSource {

    private val dictionaryChannel = Channel<List<DictionaryLogEntryData>>()

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long {
        val newId = mockDictionaries.size + 1
        mockDictionaries.add(
            newDictionary.toDictionaryLogEntryData(id = newId)
        )
        dictionaryChannel.send(mockDictionaries)
        return (newId).toLong()
    }

    override fun getDictionaries(): Flow<List<DictionaryLogEntryData>> = flow {
        emit(mockDictionaries)
        dictionaryChannel.consumeEach {
            emit(it)
        }
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

    override suspend fun deleteDictionaryById(id: Int): OperationResult {
        TODO("Not yet implemented")
    }

}

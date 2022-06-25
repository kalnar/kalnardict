package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class DictionaryLogDaoMock : DictionaryLogDao {
    private val mockDictionaryInfoMap = hashMapOf<Int, DictionaryLogEntry>()
    private val dictionaryChannel = Channel<HashMap<Int, DictionaryLogEntry>>()

    init {
        GlobalScope.launch {
            dictionaryChannel.consumeEach {
            }
        }
    }

    override suspend fun insertDictionary(sampleDictionaryLogEntry: DictionaryLogEntry): Long {
        val newId = mockDictionaryInfoMap.entries.size + 1
        mockDictionaryInfoMap[newId] = sampleDictionaryLogEntry
        runBlocking {
            dictionaryChannel.send(mockDictionaryInfoMap)
        }
        return newId.toLong()
    }

    override suspend fun getDictionaryById(id: Int): DictionaryLogEntry? {
        return mockDictionaryInfoMap[id]
    }

    override fun getDictionaries(): Flow<List<DictionaryLogEntry>> = flow {
        emit(mockDictionaryInfoMap.values.toList())
        dictionaryChannel.consumeEach {
            emit(mockDictionaryInfoMap.values.toList())
        }
    }

    override suspend fun deleteDictionary(id: DictionaryLogId): Int {
        mockDictionaryInfoMap.remove(id.value)
        return 1
    }

}
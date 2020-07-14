package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry

class DictionaryLogDaoMock : DictionaryLogDao {
    private val mockDictionaryInfoMap = hashMapOf<Int, DictionaryLogEntry>()
    override suspend fun insertDictionary(sampleDictionaryLogEntry: DictionaryLogEntry): Long {
        val newId = mockDictionaryInfoMap.entries.size + 1
        mockDictionaryInfoMap[newId] = sampleDictionaryLogEntry
        return newId.toLong()
    }

    override suspend fun getDictionaryById(id: Int): DictionaryLogEntry? {
        return mockDictionaryInfoMap[id]
    }

    override suspend fun getDictionaries(): List<DictionaryLogEntry> {
        return mockDictionaryInfoMap.values.toList()
    }

}
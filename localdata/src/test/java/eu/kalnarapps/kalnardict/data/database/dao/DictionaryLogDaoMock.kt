package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry

class DictionaryLogDaoMock : DictionaryLogDao {
    private val mockDictionaryInfoMap = hashMapOf<Int, DictionaryLogEntry>()
    override fun insertDictionary(sampleDictionaryLogEntry: DictionaryLogEntry) {
        mockDictionaryInfoMap[sampleDictionaryLogEntry.id] = sampleDictionaryLogEntry
    }

    override fun getDictionaryById(id: Int): DictionaryLogEntry? {
        return mockDictionaryInfoMap[id]
    }

    override fun getDictionaries(): List<DictionaryLogEntry> {
        return mockDictionaryInfoMap.values.toList()
    }

}
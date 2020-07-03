package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.data.dao.ConfigurationDao

class MockConfigurationDao : ConfigurationDao {
    private var lastDictId = 0
    override suspend fun getLastDictionaryId(): Int {
        return lastDictId
    }

    override suspend fun updateLastDictionary(dictionaryId: Int) {
        lastDictId = dictionaryId
    }
}
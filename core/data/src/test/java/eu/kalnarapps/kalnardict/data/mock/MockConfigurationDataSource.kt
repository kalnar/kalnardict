package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.data.dao.ConfigurationDataSource

class MockConfigurationDataSource : ConfigurationDataSource {
    private var lastDictId = 0
    override suspend fun getLastDictionaryId(): Int {
        return lastDictId
    }

    override suspend fun updateLastDictionary(dictionaryId: Int) {
        lastDictId = dictionaryId
    }
}
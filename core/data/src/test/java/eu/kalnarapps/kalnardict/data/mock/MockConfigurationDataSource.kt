package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.data.datasources.configuration.ConfigurationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockConfigurationDataSource :
    ConfigurationDataSource {
    private var lastDictId = 0
    override fun getLastDictionaryId(): Flow<Int> {
        return flowOf(lastDictId)
    }

    override suspend fun updateLastDictionary(dictionaryId: Int) {
        lastDictId = dictionaryId
    }
}
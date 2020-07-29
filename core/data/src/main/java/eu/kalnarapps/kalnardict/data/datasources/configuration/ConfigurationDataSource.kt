package eu.kalnarapps.kalnardict.data.datasources.configuration

import kotlinx.coroutines.flow.Flow

interface ConfigurationDataSource {
    fun getLastDictionaryId(): Flow<Int>
    suspend fun updateLastDictionary(dictionaryId: Int)
}
package eu.kalnarapps.kalnardict.data.datasources.configuration

interface ConfigurationDataSource {
    suspend fun getLastDictionaryId(): Int
    suspend fun updateLastDictionary(dictionaryId: Int)
}
package eu.kalnarapps.kalnardict.data.datasources

interface ConfigurationDataSource {
    suspend fun getLastDictionaryId(): Int
    suspend fun updateLastDictionary(dictionaryId: Int)
}
package eu.kalnarapps.kalnardict.data.dao

interface ConfigurationDataSource {
    suspend fun getLastDictionaryId(): Int
    suspend fun updateLastDictionary(dictionaryId: Int)
}
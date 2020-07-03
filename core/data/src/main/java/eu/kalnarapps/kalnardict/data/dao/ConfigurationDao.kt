package eu.kalnarapps.kalnardict.data.dao

interface ConfigurationDao {
    suspend fun getLastDictionaryId(): Int
    suspend fun updateLastDictionary(dictionaryId: Int)
}
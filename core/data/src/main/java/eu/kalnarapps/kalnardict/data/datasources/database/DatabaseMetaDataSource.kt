package eu.kalnarapps.kalnardict.data.datasources.database

import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData

interface DatabaseMetaDataSource {
    suspend fun addDatabase(path: String)
    suspend fun getDatabases(): List<DictionaryLogEntryData>
}
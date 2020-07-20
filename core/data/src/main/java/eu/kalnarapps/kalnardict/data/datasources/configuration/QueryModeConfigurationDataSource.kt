package eu.kalnarapps.kalnardict.data.datasources.configuration

import kotlinx.coroutines.flow.Flow

interface QueryModeConfigurationDataSource {
    fun getLastQueryModeId(): Flow<Int>
    suspend fun updateQueryMode(queryModeId: Int)
}
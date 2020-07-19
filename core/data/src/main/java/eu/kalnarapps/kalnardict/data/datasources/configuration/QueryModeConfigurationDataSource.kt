package eu.kalnarapps.kalnardict.data.datasources.configuration

interface QueryModeConfigurationDataSource {
    suspend fun getLastQueryModeId(): Int
    suspend fun updateQueryMode(queryModeId: Int)
}
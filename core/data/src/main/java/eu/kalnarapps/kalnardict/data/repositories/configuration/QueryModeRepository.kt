package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QueryModeRepository(
    private val queryModeConfigurationDataSource: QueryModeConfigurationDataSource
) : QueryModeConfigurationRepository {
    override fun getCurrentQueryMode(): Flow<QueryMode> {
        return queryModeConfigurationDataSource.getLastQueryModeId().map {
            QueryMode.fromId(it)
        }
    }

    override suspend fun updateCurrentQueryMode(queryMode: QueryMode) {
        queryModeConfigurationDataSource.updateQueryMode(queryModeId = queryMode.value)
    }
}
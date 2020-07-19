package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode

class QueryModeRepository(
    private val queryModeConfigurationDataSource: QueryModeConfigurationDataSource
) : QueryModeConfigurationRepository {
    override suspend fun getCurrentQueryMode(): QueryMode {
        return QueryMode.fromId(
            queryModeConfigurationDataSource.getLastQueryModeId()
        )
    }

    override suspend fun updateCurrentQueryMode(queryMode: QueryMode) {
        queryModeConfigurationDataSource.updateQueryMode(queryModeId = queryMode.value)
    }
}
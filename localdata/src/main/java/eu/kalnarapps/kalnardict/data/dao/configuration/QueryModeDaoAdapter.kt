package eu.kalnarapps.kalnardict.data.dao.configuration

import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class QueryModeDaoAdapter(
    private val configurationPropertyDao: ConfigurationPropertyDao
) : QueryModeConfigurationDataSource {

    override fun getLastQueryModeId(): Flow<Int> {
        return configurationPropertyDao.getFlowPropertyByKey(
            ConfigurationPropertyKey.QUERY_MATCH_MODE.key
        ).map {
            it.propertyValue.toIntOrNull() ?: DataBaseConstants.UNINITIALIZED_INT_PROPERTY
        }
    }

    override suspend fun updateQueryMode(queryModeId: Int) {
        configurationPropertyDao.updateProperty(
            ConfigurationProperty(
                propertyKey = ConfigurationPropertyKey.QUERY_MATCH_MODE.key,
                propertyValue = queryModeId.toString()
            )
        )
    }
}


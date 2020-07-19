package eu.kalnarapps.kalnardict.data.dao.configuration

import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants


class QueryModeDaoAdapter(
    private val configurationPropertyDao: ConfigurationPropertyDao
) : QueryModeConfigurationDataSource {

    override suspend fun getLastQueryModeId(): Int {
        return configurationPropertyDao.getPropertyByKey(
            ConfigurationPropertyKey.QUERY_MATCH_MODE.key
        )?.propertyValue?.toIntOrNull() ?: DataBaseConstants.UNINITIALIZED_INT_PROPERTY
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


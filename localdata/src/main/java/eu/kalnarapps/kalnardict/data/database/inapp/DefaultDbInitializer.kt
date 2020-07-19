package eu.kalnarapps.kalnardict.data.database.inapp

import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants

object DefaultDbInitializer : AppDbDataInitializer {
    override fun populateInitialData(db: AppDatabase) {
        populateConfigurationProperties(db.configurationPropertyDao())
    }

    private fun populateConfigurationProperties(configDao: ConfigurationPropertyDao) {
        for (propertyEnum in ConfigurationPropertyKey.values()) {
            when (propertyEnum) {
                ConfigurationPropertyKey.LAST_DICTIONARY -> {
                    configDao.insertProperty(
                        property = ConfigurationProperty(
                            propertyKey = propertyEnum.key,
                            propertyValue = DataBaseConstants.UNINITIALIZED_PROPERTY
                        )
                    )
                }
                ConfigurationPropertyKey.QUERY_MATCH_MODE -> {
                    configDao.insertProperty(
                        property = ConfigurationProperty(
                            propertyKey = propertyEnum.key,
                            propertyValue = DataBaseConstants.DEFAULT_QUERY_MODE_ID
                        )
                    )
                }
            }.exhaustive
        }
    }
}
package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants


class ConfigurationPropertyDaoAdapter(
    private val configurationPropertyDao: ConfigurationPropertyDao
) : ConfigurationDataSource {
    override suspend fun getLastDictionaryId(): Int {
        return configurationPropertyDao.getPropertyByKey(
            ConfigurationPropertyKey.LAST_DICTIONARY.key
        )?.propertyValue?.toIntOrNull() ?: DataBaseConstants.UNINITIALIZED_INT_PROPERTY
    }

    override suspend fun updateLastDictionary(dictionaryId: Int) {
        configurationPropertyDao.updateProperty(
            ConfigurationProperty(
                propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
                propertyValue = dictionaryId.toString()
            )
        )
    }
}

enum class ConfigurationPropertyKey(val key: String) {
    LAST_DICTIONARY("last_dictionary_id");

    companion object {
        fun fromString(type: String): ConfigurationPropertyKey? {
            return values().associateBy(ConfigurationPropertyKey::key)[type]
        }
    }
}
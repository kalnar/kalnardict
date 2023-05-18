package eu.kalnarapps.kalnardict.data.dao.configuration

import eu.kalnarapps.kalnardict.data.datasources.configuration.ConfigurationDataSource
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants.UNINITIALIZED_INT_PROPERTY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class ConfigurationPropertyDaoAdapter(
    private val configurationPropertyDao: ConfigurationPropertyDao
) : ConfigurationDataSource {
    override fun getLastDictionaryId(): Flow<Int> {
        return configurationPropertyDao.getFlowPropertyByKey(
            ConfigurationPropertyKey.LAST_DICTIONARY.key
        )
            .filter { it.propertyValue.toIntOrNull() != null }
            .map { it.propertyValue.toInt() }
    }

    override suspend fun getLastDictionaryIdOneShot(): Int {
        return configurationPropertyDao.getPropertyByKey(
            ConfigurationPropertyKey.LAST_DICTIONARY.key
        )
            .takeIf { it?.propertyValue?.toIntOrNull() != null }
            .let { it?.propertyValue?.toInt() ?: UNINITIALIZED_INT_PROPERTY }
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
    LAST_DICTIONARY("last_dictionary_id"),
    QUERY_MATCH_MODE("last_query_match_mode_id");

    companion object {
        fun fromString(type: String): ConfigurationPropertyKey? {
            return values().associateBy(ConfigurationPropertyKey::key)[type]
        }
    }
}
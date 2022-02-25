package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.EnumMap

class ConfigurationPropertyMockDao(
    propertyList: List<ConfigurationProperty> = emptyList()
) : ConfigurationPropertyDao {
    private val propertyRegistry: EnumMap<ConfigurationPropertyKey, String> =
        EnumMap(ConfigurationPropertyKey::class.java)

    init {
        for (property in propertyList) {
            ConfigurationPropertyKey.fromString(property.propertyKey)?.let {
                propertyRegistry[it] = property.propertyValue
            }
        }
    }

    override suspend fun getPropertyByKey(key: String): ConfigurationProperty? {
        return ConfigurationPropertyKey.fromString(key)?.let {
            ConfigurationProperty(
                propertyKey = it.key,
                propertyValue = propertyRegistry[it] ?: DataBaseConstants.UNINITIALIZED_INT_PROPERTY.toString()
            )
        }
    }

    override fun getFlowPropertyByKey(key: String): Flow<ConfigurationProperty> = flow {
        getPropertyByKey(key)?.let {
            emit(it)
        }
    }

    override suspend fun updateProperty(property: ConfigurationProperty) {
        propertyRegistry[ConfigurationPropertyKey.fromString(property.propertyKey)] =
            property.propertyValue
    }

    override fun insertProperty(property: ConfigurationProperty) {
        propertyRegistry[ConfigurationPropertyKey.fromString(property.propertyKey)] =
            property.propertyValue
    }
}
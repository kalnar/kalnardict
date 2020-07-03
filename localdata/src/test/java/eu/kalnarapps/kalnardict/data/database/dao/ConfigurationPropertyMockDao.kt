package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
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

    override fun getPropertyByKey(key: String): ConfigurationProperty? {
        return ConfigurationPropertyKey.fromString(key)?.let {
            ConfigurationProperty(
                propertyKey = it.key,
                propertyValue = propertyRegistry[it] ?: DataBaseConstants.UNINITIALIZED_PROPERTY
            )
        }
    }

    override fun updateProperty(property: ConfigurationProperty) {
        propertyRegistry[ConfigurationPropertyKey.fromString(property.propertyKey)] =
            property.propertyValue
    }
}
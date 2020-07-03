package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants

@Dao
interface ConfigurationPropertyDao {

    // @formatter:off
    @Query(
        """ 
            SELECT * FROM 
                ${DataBaseConstants.CONFIGURATION_PROPERTY_TABLE_NAME} 
            WHERE property_key = :key LIMIT 1
        """
    )
    // @formatter:on
    fun getPropertyByKey(key: String): ConfigurationProperty?

    @Update
    fun updateProperty(property: ConfigurationProperty)

}
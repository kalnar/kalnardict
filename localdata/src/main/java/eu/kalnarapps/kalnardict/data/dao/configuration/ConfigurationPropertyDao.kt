package eu.kalnarapps.kalnardict.data.dao.configuration

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.flow.Flow

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
    suspend fun getPropertyByKey(key: String): ConfigurationProperty?

    // @formatter:off
    @Query(
        """ 
            SELECT * FROM 
                ${DataBaseConstants.CONFIGURATION_PROPERTY_TABLE_NAME} 
            WHERE property_key = :key LIMIT 1
        """
    )
    // @formatter:on
    fun getFlowPropertyByKey(key: String): Flow<ConfigurationProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProperty(property: ConfigurationProperty)

    @Insert
    fun insertProperty(property: ConfigurationProperty)
}
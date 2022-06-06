package eu.kalnarapps.kalnardict.data.dao.external.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants.EXTERNAL_DATABASE_TABLE_NAME
import eu.kalnarapps.kalnardict.data.entities.DbColumns
import eu.kalnarapps.kalnardict.data.entities.ExternalDatabase

@Dao
interface ExternalDbDao {

    @Query("SELECT ${DbColumns.EXTERNAL_DATABASE_COLUMN_PATH} FROM $EXTERNAL_DATABASE_TABLE_NAME")
    suspend fun getDatabases(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDatabase(database: ExternalDatabase): Long
}
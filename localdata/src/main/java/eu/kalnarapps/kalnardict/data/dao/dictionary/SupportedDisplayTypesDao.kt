package eu.kalnarapps.kalnardict.data.dao.dictionary

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.data.entities.DictionaryDisplayTypeData
import eu.kalnarapps.kalnardict.data.entities.SupportedDictionaryDisplayType
import kotlinx.coroutines.flow.Flow

interface SupportedDisplayTypesDao {

    @Query("SELECT ${DataBaseConstants.DISPLAY_TYPE_COLUMN} FROM ${DataBaseConstants.DISPLAY_TYPE_TABLE_NAME} where dictionary_id = :dictionaryId")
    fun getSupportedDisplayTypesForDictionaryWithId(
        dictionaryId: Int
    ): Flow<List<DictionaryDisplayTypeData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDisplayType(dictionaryDisplayType: SupportedDictionaryDisplayType): Long
}

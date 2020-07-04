package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry

@Dao
interface DictionaryLogDao {
    @Insert
    suspend fun insertDictionary(sampleDictionaryLogEntry: DictionaryLogEntry)

    @Query("SELECT * FROM dictionary_log WHERE id = :id LIMIT 1")
    suspend fun getDictionaryById(id: Int): DictionaryLogEntry?

    @Query("SELECT * FROM dictionary_log")
    suspend fun getDictionaries(): List<DictionaryLogEntry>
}
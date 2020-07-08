package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.Language

@Dao
interface LanguageDao {

    @Query("SELECT * FROM Language WHERE id = :idString LIMIT 1")
    suspend fun getLanguageById(idString: String): Language?

    @Query("SELECT * FROM Language")
    suspend fun getLanguages(): List<Language>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLanguage(language: Language): Long
}


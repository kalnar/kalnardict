package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.Language

@Dao
interface LanguageDao {

    @Query("SELECT * FROM Language WHERE id = :idString LIMIT 1")
    suspend fun getLanguageById(idString: String): Language?

}


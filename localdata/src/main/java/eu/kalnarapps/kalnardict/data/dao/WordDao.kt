package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Query
import eu.kalnarapps.kalnardict.data.entities.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getAll(): List<Word>

    @Query("SELECT * FROM word WHERE base_form LIKE :queryString LIMIT 100")
    suspend fun getByQuery(queryString: String): List<Word>

    @Query("INSERT into word values(1,'asztal', 'table')")
    fun insertData()

}
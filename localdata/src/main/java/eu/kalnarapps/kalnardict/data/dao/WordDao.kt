package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import eu.kalnarapps.kalnardict.data.DictDao
import eu.kalnarapps.kalnardict.data.DictEntry
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogWithWords
import eu.kalnarapps.kalnardict.data.entities.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getAll(): List<Word>

    @Query("SELECT * FROM word WHERE base_form LIKE :queryString LIMIT 100")
    suspend fun getByQuery(queryString: String): List<Word>

    @Query("INSERT into word values(1,'asztal', 'table')")
    fun insertData()

    @Insert
    fun insertWord(word: Word)

    @Transaction
    @Query(
        "SELECT * FROM Word WHERE base_form LIKE :queryString and dictionary_id = :dictId LIMIT 100"
    )
    fun getWordsByQueryInDictionary(queryString: String, dictId: Int): List<DictionaryLogWithWords>

}

class Dao(
    private val wordDao: WordDao
) : DictDao {
    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        wordDao.insertWord(
            dictEntry.toWord()
        )
    }
}

private fun DictEntry.toWord(): Word {
    return Word(
        id = getId(),
        baseForm = getBaseForm(),
        alternativeBaseForm = getAlternativeBaseForm(),
        translation = getTranslation(),
        dictionaryId = getDictionaryId()
    )
}

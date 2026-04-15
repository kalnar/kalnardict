package eu.kalnarapps.kalnardict.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogWithWords
import eu.kalnarapps.kalnardict.data.entities.Word

@Dao
interface WordDao {

    @Query(
        """SELECT 
        id, base_form, base_form_alt, dictionary_id
            FROM 
        word 
            WHERE 
        base_form LIKE :queryString ESCAPE '\'
            AND
        dictionary_id = :dictionaryId LIMIT 100"""
    )
    suspend fun getByQuery(queryString: String, dictionaryId: Int): List<Word.WordInfo>

    @Query(
        """SELECT 
        id, base_form, base_form_alt, dictionary_id
            FROM 
        word 
            WHERE 
        base_form_alt LIKE :queryString ESCAPE '\'
            AND
        dictionary_id = :dictionaryId LIMIT 100"""
    )
    suspend fun getByQueryAlternative(queryString: String, dictionaryId: Int): List<Word.WordInfo>

    @Query(
        """SELECT 
        id, translation, dictionary_id 
            FROM 
        word 
            WHERE 
        id = :wordId 
            and 
        dictionary_id = :dictionaryId LIMIT 1
        """
    )
    suspend fun getTranslationByIds(wordId: Int, dictionaryId: Int): Word.TranslationInfo?

    @Insert
    suspend fun insertWord(word: Word)

    @Insert
    suspend fun insertWords(wordsToInsert: List<Word>): List<Long>

    @Transaction
    @Query(
        "SELECT " +
                "dictionary_log.id, dictionary_name, language_from, " +
                "language_to, description, version, " +
                "word.id, base_form, base_form_alt, translation, dictionary_id" +
                " FROM Word join dictionary_log ON dictionary_log.id = dictionary_id " +
                "WHERE base_form LIKE :queryString and dictionary_id = :dictId LIMIT 100;"
    )
    suspend fun getWordsByQueryInDictionary(
        queryString: String,
        dictId: Int
    ): List<DictionaryLogWithWords>

}


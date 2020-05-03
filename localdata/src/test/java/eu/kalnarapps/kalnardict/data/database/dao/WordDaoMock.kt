package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogWithWords
import eu.kalnarapps.kalnardict.data.entities.Word


class WordDaoMock : WordDao {
    private val words = ArrayList<Word>().apply {
        add(sampleTableInHungarian)
    }
    private val dictionaries = ArrayList<DictionaryLogEntry>().apply {
        add(sampleDictionaryLogEntry)
    }

    override suspend fun getByQuery(queryString: String): List<Word> {
        return words.filter { it.baseForm.contains(queryString) }
    }

    override fun insertWord(word: Word) {
        words.add(word)
    }

    override fun insertWords(newWords: List<Word>) {
        this.words.addAll(newWords)
    }

    override fun getWordsByQueryInDictionary(
        queryString: String,
        dictId: Int
    ): List<DictionaryLogWithWords> {
        return words.filter {
            it.baseForm.contains(queryString)
        }.groupBy {
            it.dictionaryId
        }.mapNotNull { idAndWords ->
            val dictionaryLog = dictionaries.find { it.id == idAndWords.key }
            if (dictionaryLog != null) {
                DictionaryLogWithWords(
                    dictionaryLog,
                    idAndWords.value
                )
            } else {
                null
            }
        }
    }

}
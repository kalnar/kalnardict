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

    override suspend fun getByQuery(queryString: String): List<Word.WordInfo> {
        return words.filter {
            it.baseForm.contains(
                queryString.run {
                    var searchText = if (this[0] == '%') {
                        substring(1)
                    } else {
                        this
                    }
                    searchText = if (searchText.last() == '%') {
                        searchText.substring(0, searchText.lastIndex)
                    } else {
                        searchText
                    }
                    searchText
                }
            )
        }.map {
            Word.WordInfo(it.id, it.baseForm, it.alternativeBaseForm, it.dictionaryId)
        }
    }

    override suspend fun getTranslationByIds(
        wordId: Int,
        dictionaryId: Int
    ): Word.TranslationInfo? {
        return words.find { it.dictionaryId == dictionaryId && it.id == wordId }?.run {
            Word.TranslationInfo(this.id, this.translation, this.dictionaryId)
        }
    }

    override suspend fun insertWord(word: Word) {
        words.add(word)
    }

    override suspend fun insertWords(newWords: List<Word>) {
        this.words.addAll(newWords)
    }

    override suspend fun getWordsByQueryInDictionary(
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
package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.DictDao
import eu.kalnarapps.kalnardict.data.DictEntry
import eu.kalnarapps.kalnardict.data.entities.Word

class DataSource(
    private val wordDao: WordDao
) : DictDao {
    override suspend fun insertDictEntry(dictEntry: DictEntry) {
        wordDao.insertWord(
            dictEntry.toWord()
        )
    }

    override suspend fun insertDictEntries(dictEntries: List<DictEntry>) {
        wordDao.insertWords(dictEntries.map { it.toWord() })
    }

    override suspend fun getDictEntryByQuery(query: String): List<DictEntry> {
        return wordDao.getByQuery(query).map { it.toDictEntry() }
    }
}

private fun Word.toDictEntry(): DictEntry {
    return object : DictEntry {
        override fun getId(): Int = id
        override fun getBaseForm(): String = baseForm
        override fun getAlternativeBaseForm(): String = alternativeBaseForm
        override fun getTranslation(): String = translation
        override fun getDictionaryId(): Int = dictionaryId
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
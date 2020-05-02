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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDictEntryByQuery(query: String): List<DictEntry> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
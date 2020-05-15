package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.DictDao
import eu.kalnarapps.kalnardict.data.DictEntry
import eu.kalnarapps.kalnardict.data.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Word

class DataSource(
    private val wordDao: WordDao,
    private val dictionaryMetaDao: DictionaryLogDao
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

    override suspend fun getDictionaries(): List<DictionaryLogEntryData> {
        return dictionaryMetaDao.getDictionaries().map {
            it.toDictionaryLogEntryData()
        }
    }
}

private fun DictionaryLogEntry.toDictionaryLogEntryData(): DictionaryLogEntryData {
    return DictionaryInfo(
        id = this.id,
        name = this.dictionaryName,
        languageFrom = this.languageFrom,
        languageTo = this.languageTo
    )
}

data class DictionaryInfo(
    override val id: Int,
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : DictionaryLogEntryData

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
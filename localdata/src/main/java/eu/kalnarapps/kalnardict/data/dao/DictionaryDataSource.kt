package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictEntry
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toWord

class DictionaryDataSource(
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

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData) {
        dictionaryMetaDao.insertDictionary(
            DictionaryLogEntry(
                dictionaryName = newDictionary.name,
                languageFrom = newDictionary.languageFrom,
                languageTo = newDictionary.languageTo
            )
        )
    }

    override suspend fun getDictionaries(): List<DictionaryLogEntryData> {
        return dictionaryMetaDao.getDictionaries().map {
            it.toDictionaryLogEntryData()
        }
    }
}


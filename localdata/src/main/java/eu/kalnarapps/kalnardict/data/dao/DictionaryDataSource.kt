package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictEntry
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toWord

class DictionaryDataSource(
    private val wordDao: WordDao,
    private val dictionaryMetaDao: DictionaryLogDao
) : DictDao {
    override suspend fun insertDictEntry(wordDataEntry: WordDataEntry) {
        wordDao.insertWord(
            wordDataEntry.toWord()
        )
    }

    override suspend fun insertDictEntries(wordDataEntries: List<WordDataEntry>) {
        wordDao.insertWords(wordDataEntries.map { it.toWord() })
    }

    override suspend fun queryWithMatchAnyWhere(query: String): List<WordDataEntry> {
        return wordDao.getByQuery("%${query}%").map { it.toDictEntry() }
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

    override suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData> {
        // TODO: to test
        return dictionaryMetaDao.getDictionaryById(id)?.let {
            DataOperationResult.Success(it.toDictionaryLogEntryData())
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionary in data source with id $id"
        )
    }
}


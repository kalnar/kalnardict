package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.RoomEntityToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData

class DictionaryDataSource(
    private val wordDao: WordDao,
    private val dictionaryMetaDao: DictionaryLogDao,
    private val wordInfoMapper: RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry>,
    private val translatedWordMapper: LocalDataToRoomEntityMapper<TranslatedWordDataEntry, Word>
) : DictDao {
    override suspend fun insertDictEntry(wordDataEntry: TranslatedWordDataEntry) {
        wordDao.insertWord(
            translatedWordMapper.toRoomEntityModel(wordDataEntry)
        )
    }

    override suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordDataEntry>) {
        wordDao.insertWords(wordDataEntries.map {
            translatedWordMapper.toRoomEntityModel(it)
        })
    }

    override suspend fun queryWithMatchAnyWhere(query: String): List<WordDataEntry> {
        return wordDao.getByQuery("%${query}%").map {
            wordInfoMapper.toLocalData(it)
        }
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

    override suspend fun getTranslationByWordAndDictionaryId(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        return wordDao.getTranslationByIds(wordId, dictionaryId)?.let {
            DataOperationResult.Success(it.translation)
        } ?: DataOperationResult.Failure(
            errorMessage = "There is no word with id $wordId in dictionary $dictionaryId. " +
                    "Possible reasons: dictionary or word does not exists or wrong " +
                    "dictionary is used."
        )
    }
}


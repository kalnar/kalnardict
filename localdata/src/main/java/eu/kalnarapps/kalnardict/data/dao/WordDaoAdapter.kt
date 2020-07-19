package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.datasources.WordDataSource
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry

class WordDaoAdapter(
    private val wordDao: WordDao,
    private val translatedWordMapper: LocalDataToRoomEntityMapper<TranslatedWordInsertEntry, Word>
) : WordDataSource {
    override suspend fun insertDictEntry(wordDataEntry: TranslatedWordInsertEntry) {
        wordDao.insertWord(
            translatedWordMapper.toRoomEntityModel(wordDataEntry)
        )
    }

    override suspend fun insertDictEntries(wordDataEntries: List<TranslatedWordInsertEntry>): OperationResult {
        val newIds = wordDao.insertWords(wordDataEntries.map {
            translatedWordMapper.toRoomEntityModel(it)
        })
        return if (newIds.contains(-1)) {
            OperationResult.Failure(
                errorMessage =
                "insert failed: the following words failed: " +
                        "${wordDataEntries.filterIndexed { index, _ -> newIds[index] == -1L }}"
            )
        } else {
            OperationResult.Success
        }
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


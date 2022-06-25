package eu.kalnarapps.kalnardict.interactors.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.interactors.Stubs
import kotlinx.coroutines.flow.Flow

class MockDictionaryRepository : DictionaryRepository {
    data class MockDictEntry(
        val word: DictWord,
        val translation: DictTranslation,
        val dictionary: Dictionary
    )

    private val listOfDictEntries: List<MockDictEntry> = listOf(
        MockDictEntry(
            word = Stubs.Words.wordTake,
            translation = Stubs.Words.wordTakeTranslationInFrench,
            dictionary = Stubs.Dictionaries.englishToFrenchDictionary
        ),
        MockDictEntry(
            word = Stubs.Words.wordPrendre,
            translation = Stubs.Words.wordPrendreTranslationInFrench,
            dictionary = Stubs.Dictionaries.frenchToFrenchDictionary
        )
    )

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        TODO("Not yet implemented")
    }

    override suspend fun importTableFromDb(importJob: ImportJob): Flow<DataOperationResult<ImportProgress>> {
        TODO("Not yet implemented")
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        TODO("Not yet implemented")
    }

    override fun readRegisteredDictionaries(): Flow<List<Dictionary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        TODO("Not yet implemented")
    }

    override suspend fun getTranslationById(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        val translation = listOfDictEntries.find {
            it.dictionary.id == dictionaryId &&
                    it.word.id == wordId
        }?.translation?.translation
        return if (translation == null) {
            DataOperationResult.Failure(
                errorMessage = Stubs.Words.wrongIdTranslationErrorMessage
            )
        } else {
            DataOperationResult.Success(translation)
        }
    }

    override suspend fun deleteDictionaryById(dictionaryId: Int): OperationResult {
        TODO("Not yet implemented")
    }
}
package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.model.MockDictEntry
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryWrapper
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale


class RegisterNewDictionaryMock(
    private val dictionaryListMock: DictionaryListMock
) : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> = flow {
        dictionaryListMock.add(
            Dictionary(
                id = dictionaryListMock.size + 1,
                languageFrom = DictLanguage(
                    name = Locale(languageFrom).displayName,
                    code = languageFrom
                ),
                languageTo = DictLanguage(
                    name = Locale(languageTo).displayName,
                    code = languageTo
                ),
                description = savingName
            )
        )
        val total = 100
        var progressIndicator = 0
        while (progressIndicator != total) {
            progressIndicator += 5
            emit(
                DataOperationResult.Success(
                    ImportProgress(
                        total,
                        progressIndicator
                    )
                )
            )
            delay(500L)
        }
    }
}

class DictionaryListMock : ArrayList<Dictionary>()

class ListDictionariesMock(
    private val dictionaryListMock: DictionaryListMock
) : ListRegisteredDictionariesUseCase {
    override suspend fun invoke(): List<Dictionary> {
        return dictionaryListMock
    }
}

class RegisterNewDictionarySuccessfullyMock : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> {
        return flow {
            emit(
                DataOperationResult.Success(ImportProgress(100, 100))
            )
        }
    }
}

class RegisterNewDictionaryMockWithFailures(
    private val listOfFailureOccasions: List<Int>
) : RegisterNewDictionaryUseCase {
    private var counter = 0
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> = flow {
        counter++
        emit(
            if (listOfFailureOccasions.contains(counter)) {
                DataOperationResult.Failure<ImportProgress>(
                    errorMessage = UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG
                )
            } else {
                DataOperationResult.Success(
                    ImportProgress(100, 100)
                )
            }
        )
    }
}

class MockSearchQueryUseCase(
    private val mockEntries: List<MockDictEntry> = emptyList(),
    private val currentDictionary: DictionaryWrapper? = null
) : SearchQueryUseCase {
    override suspend fun invokeWith(query: String): List<DictWord> {
        return mockEntries.filter {
            it.dictionary.id == currentDictionary?.currentDictionary?.dictionary?.id &&
                    it.word.baseForm.contains(query)
        }.map {
            it.word
        }
    }

}

class MockChangeDictLanguageUseCase(
    private var currentDictionary: DictionaryWrapper? = null,
    private val listOfDictionaries: List<Dictionary> = emptyList()
) : ChangeDictLanguageUseCase {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        return listOfDictionaries.find { it.id == dictionaryId }?.let {
            currentDictionary?.currentDictionary = CurrentDictionary.SetDictionary(it)
            OperationResult.Success
        } ?: OperationResult.Failure(
            errorMessage = "no dictionary found with id: $dictionaryId"
        )
    }
}

class MockGetLanguageUseCase(
    private val currentDictionary: CurrentDictionary = CurrentDictionary.DictionaryNotSet
) : GetLanguageUseCase {
    override suspend fun invoke(): CurrentDictionary {
        return currentDictionary
    }
}

class MockGetDictionaryUseCase(
    private val currentDictionary: DictionaryWrapper
) : GetLanguageUseCase {
    override suspend fun invoke(): CurrentDictionary {
        return currentDictionary.currentDictionary
    }
}

class MockGetTranslationUseCase(
    private val mockEntries: List<MockDictEntry>,
    private val currentDictionary: CurrentDictionary
) : GetTranslationUseCase {
    override suspend fun invoke(wordId: Int): DataOperationResult<String> {
        val foundTranslation = mockEntries.find {
            currentDictionary is CurrentDictionary.SetDictionary &&
                    it.dictionary == currentDictionary.dictionary && it.word.id == wordId
        }
        return if (foundTranslation == null) {
            DataOperationResult.Failure(
                errorMessage = UiUnitTestStubs.WRONG_WORD_ID_FOR_DICTIONARY
            )
        } else {
            DataOperationResult.Success(foundTranslation.translation.translation)
        }
    }

}
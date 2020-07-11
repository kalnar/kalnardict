package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.model.MockDictEntry
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
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
    ): OperationResult {
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
        return OperationResult.Success
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
    ): OperationResult = OperationResult.Success
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
    ): OperationResult {
        counter++
        return if (listOfFailureOccasions.contains(counter)) {
            OperationResult.Failure(
                errorMessage = UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG
            )
        } else {
            OperationResult.Success
        }
    }
}

class MockSearchQueryUseCase : SearchQueryUseCase {
    override suspend fun invokeWith(query: String): List<DictWord> {
        return emptyList()
    }

}

class MockChangeDictLanguageUseCase : ChangeDictLanguageUseCase {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        return OperationResult.Success
    }
}

class MockGetLanguageUseCase(
    private val currentDictionary: CurrentDictionary = CurrentDictionary.DictionaryNotSet
) : GetLanguageUseCase {
    override suspend fun invoke(): CurrentDictionary {
        return currentDictionary
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
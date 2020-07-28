package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.interactors.mock.MockConfigurationRepository
import eu.kalnarapps.kalnardict.interactors.mock.MockDictionaryRepository
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTranslationTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun return_translation_when_called_with_available_word_id() {
        testCoroutineRule.runBlockingTest {

            test_translation_when_called_with_available_word_id(
                wordId = Stubs.Words.wordTakeId,
                currentDictionary = CurrentDictionary.SetDictionary(
                    Stubs.Dictionaries.englishToFrenchDictionary
                ),
                expectedTranslation = Stubs.Words.wordTakeFrenchTranslationText
            )

        }

    }

    @Test
    fun return_translation_when_called_with_available_word_id_for_various_dictionaries() {
        testCoroutineRule.runBlockingTest {
            test_translation_when_called_with_available_word_id(
                wordId = Stubs.Words.wordTakeId,
                currentDictionary = CurrentDictionary.SetDictionary(
                    Stubs.Dictionaries.englishToFrenchDictionary
                ),
                expectedTranslation = Stubs.Words.wordTakeFrenchTranslationText
            )
            test_translation_when_called_with_available_word_id(
                wordId = Stubs.Words.wordPrendreId,
                currentDictionary = CurrentDictionary.SetDictionary(
                    Stubs.Dictionaries.frenchToFrenchDictionary
                ),
                expectedTranslation = Stubs.Words.wordPrendreFrenchTranslationText
            )
        }

    }

    @Test
    fun return_operation_failure_when_getting_translation_with_wrong_id() {
        testCoroutineRule.runBlockingTest {
            val getTranslation = GetTranslation(
                configurationRepository = MockConfigurationRepository(
                    CurrentDictionary.SetDictionary(Stubs.Dictionaries.englishToFrenchDictionary)
                ),
                repository = MockDictionaryRepository()
            )
            // when
            val result = getTranslation(Stubs.Words.wrongWordId)

            // then
            assertThat(
                result,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
            check(result is DataOperationResult.Failure<String>)
            assertThat(
                result.errorMessage,
                equalTo(Stubs.Words.wrongIdTranslationErrorMessage)
            )

        }
    }

    private suspend fun test_translation_when_called_with_available_word_id(
        wordId: Int,
        currentDictionary: CurrentDictionary,
        expectedTranslation: String
    ) {
        testCoroutineRule.runBlockingTest {
            val getTranslation = GetTranslation(
                configurationRepository = MockConfigurationRepository(currentDictionary),
                repository = MockDictionaryRepository()
            )
            // when
            val result = getTranslation(wordId)

            // then
            assertThat(
                result,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(result is DataOperationResult.Success<String>)
            assertThat(
                result.data,
                equalTo(expectedTranslation)
            )
        }
    }
}


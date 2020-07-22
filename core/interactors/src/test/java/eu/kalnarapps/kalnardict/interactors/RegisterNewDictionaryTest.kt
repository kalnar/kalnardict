package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.interactors.test.anyNonNull
import eu.kalnarapps.kalnardict.interactors.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class RegisterNewDictionaryTestNew {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dictionaryRepository = mock(DictionaryRepository::class.java)
    private val stubLanguageRepository = mock(LanguageRepository::class.java)

    private suspend fun setUp() {
        doAnswer { invocationOnMock ->
            val language: DictLanguage? =
                Stubs.Languages.frenchAndEnglish.find { it.code == (invocationOnMock.arguments[0] as String) }
            if (language != null) {
                DataOperationResult.Success(language)
            } else {
                DataOperationResult.Failure<DictLanguage>("")
            }
        }.`when`(stubLanguageRepository).getLanguageById(ArgumentMatchers.anyString())
    }

    @Test
    fun when_register_new_dictionary_with_valid_table_call_import_table() {
        testCoroutineRule.runBlockingTest {
            // given 3 languages and db table is valid

            setUp()

            val registerNewDictionary = RegisterNewDictionary(
                dictionaryRepository,
                stubLanguageRepository
            )

            // when calling registerNewDictionary
            registerNewDictionary(
                dbUri = Stubs.RegisteringDictionary.dbUriStub,
                originalName = Stubs.RegisteringDictionary.originalTableName,
                languageFrom = Stubs.RegisteringDictionary.sourceLangCode,
                languageTo = Stubs.RegisteringDictionary.destinationLangCode,
                savingName = Stubs.RegisteringDictionary.dictionaryName
            )

            // then we start importing
            verify(dictionaryRepository).importTableFromDb(anyNonNull())

        }
    }

    @Test
    fun when_register_new_dictionary_with_invalid_language_emit_failure() {
        testCoroutineRule.runBlockingTest {
            // given 3 languages and db table is valid

            setUp()

            val registerNewDictionary = RegisterNewDictionary(
                dictionaryRepository,
                stubLanguageRepository
            )

            // when calling registerNewDictionary then emit failure
            registerNewDictionary(
                dbUri = Stubs.RegisteringDictionary.dbUriStub,
                originalName = Stubs.RegisteringDictionary.originalTableName,
                languageFrom = Stubs.Languages.russian.code,
                languageTo = Stubs.RegisteringDictionary.destinationLangCode,
                savingName = Stubs.RegisteringDictionary.dictionaryName
            ).test(scope = this@runBlockingTest)
                .assertThat(
                    { it.last() },
                    IsInstanceOf(DataOperationResult.Failure::class.java)
                )
                .finish()

        }
    }

}


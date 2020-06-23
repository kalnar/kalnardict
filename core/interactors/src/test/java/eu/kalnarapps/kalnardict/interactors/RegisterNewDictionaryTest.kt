package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsIterableContaining
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class RegisterNewDictionaryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun register_new_dictionary() {
        testCoroutineRule.runBlockingTest {

            val dictionaryRepository = StubDictionaryRepository()
            val stubLanguageRepository = StubLanguageRepository()
            val registerNewDictionary = RegisterNewDictionary(
                dictionaryRepository,
                stubLanguageRepository
            )
            assertThat(
                ListRegisteredDictionaries(
                    dictionaryRepository
                ).invoke(),
                IsEmptyCollection()
            )

            registerNewDictionary(
                dbUri = Stubs.RegisteringDictionary.dbUriStub,
                originalName = Stubs.RegisteringDictionary.originalTableName,
                languageFrom = Stubs.RegisteringDictionary.sourceLangCode,
                languageTo = Stubs.RegisteringDictionary.destinationLangCode,
                savingName = Stubs.RegisteringDictionary.dictionaryName
            )

            // then
            val dictionaries = ListRegisteredDictionaries(
                dictionaryRepository
            ).invoke()

            assertThat(
                dictionaries,
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "description",
                        equalTo(Stubs.RegisteringDictionary.dictionaryName)
                    )
                )
            )

        }
    }

}
package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.interactors.mock.MockLanguageRepository
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListAvailableLanguagesTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun return_empty_list_if_no_language_has_been_added_yet() {
        testCoroutineRule.runBlockingTest {

            // given there are no languages

            val useCase = ListAvailableLanguages(
                MockLanguageRepository(emptyList())
            )

            assertThat(
                useCase(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun return_dictionaries_when_available() {
        testCoroutineRule.runBlockingTest {

            // given there are 2 languages registered: en,fr
            val languages = listOf(
                Stubs.Languages.english,
                Stubs.Languages.french
            )

            val useCase = ListAvailableLanguages(
                MockLanguageRepository(languages)
            )

            assertThat(
                useCase(),
                IsIterableContainingInAnyOrder(
                    languages.map { equalTo(it) }
                )
            )
        }
    }
}
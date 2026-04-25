package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.interactors.mock.MockLanguageRepository
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.Test


class ListAvailableLanguagesTest {

    @Test
    fun return_empty_list_if_no_language_has_been_added_yet() {
        runTest {

            // given there are no languages

            val useCase = ListAvailableLanguages(
                MockLanguageRepository()
            )

            assertThat(
                useCase(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun return_dictionaries_when_available() {
        runTest {

            // given there are 2 languages registered: en,fr
            val languages = listOf(
                Stubs.Languages.english,
                Stubs.Languages.french
            )

            val useCase = ListAvailableLanguages(
                MockLanguageRepository(ArrayList(languages))
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
package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataDao
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class KalnarLanguageRepositoryTest {

    private val repository =
        KalnarLanguageRepository(
            MockLanguageDataDao()
        )
    private val testCoroutineScope = TestCoroutineScope()

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun find_language_by_id() {
        testCoroutineScope.runBlockingTest {
            val languageFetch = repository.getLanguageById(Stubs.Languages.french.code)
            assertThat(
                languageFetch,
                instanceOf(DataOperationResult.Success::class.java)
            )
            check(languageFetch is DataOperationResult.Success)
            assertThat(
                languageFetch.data,
                equalTo(Stubs.Languages.french)
            )
        }
    }

    @Test
    fun fail_to_find_language_by_id() {
        testCoroutineScope.runBlockingTest {
            val languageFetch = repository.getLanguageById(Stubs.Languages.nonExistingLanguageId)
            assertThat(
                languageFetch,
                instanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    @Test
    fun get_available_languages_when_available() {
        testCoroutineScope.runBlockingTest {
            val expectedLanguages = Stubs.Languages.frenchAndEnglish
            val repository = KalnarLanguageRepository(
                MockLanguageDataDao(expectedLanguages)
            )
            assertThat(
                repository.getLanguages(),
                IsIterableContainingInAnyOrder(
                    expectedLanguages.map { equalTo(it) }
                )
            )
        }
    }

    @Test
    fun get_empty_list_of_language_when_there_are_none() {
        testCoroutineScope.runBlockingTest {
            val expectedLanguages = emptyList<DictLanguage>()
            val repository = KalnarLanguageRepository(
                MockLanguageDataDao(expectedLanguages)
            )
            assertThat(
                repository.getLanguages(),
                IsEmptyCollection()
            )
        }
    }
}
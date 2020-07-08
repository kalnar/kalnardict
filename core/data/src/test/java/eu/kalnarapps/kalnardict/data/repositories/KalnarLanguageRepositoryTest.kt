package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataDao
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.collection.IsIterableWithSize
import org.hamcrest.core.IsCollectionContaining
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsIterableContaining
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.math.exp

@ExperimentalCoroutinesApi
class KalnarLanguageRepositoryTest {

    private val repository =
        KalnarLanguageRepository(
            MockLanguageDataDao(),
            LanguageDataMapper()
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
                MockLanguageDataDao(ArrayList(expectedLanguages)),
                LanguageDataMapper()
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
                MockLanguageDataDao(ArrayList(expectedLanguages)),
                LanguageDataMapper()
            )
            assertThat(
                repository.getLanguages(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun add_new_language_with_unique_id() {
        testCoroutineScope.runBlockingTest {
            val expectedLanguages = Stubs.Languages.frenchAndEnglish.plus(Stubs.Languages.russian)
            val repository = KalnarLanguageRepository(
                MockLanguageDataDao(ArrayList(Stubs.Languages.frenchAndEnglish)),
                LanguageDataMapper()
            )

            val result = repository.addNewLanguage(Stubs.Languages.russian)

            assertThat(
                result,
                IsInstanceOf(OperationResult.Success::class.java)
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
    fun attempt_add_new_language_with_non_unique_id_and_fail() {
        testCoroutineScope.runBlockingTest {
            val expectedLanguages = Stubs.Languages.frenchAndEnglish
            val repository = KalnarLanguageRepository(
                MockLanguageDataDao(ArrayList(expectedLanguages)),
                LanguageDataMapper()
            )

            val result = repository.addNewLanguage(Stubs.Languages.french)

            assertThat(
                result,
                IsInstanceOf(OperationResult.Failure::class.java)
            )

            assertThat(
                repository.getLanguages(),
                IsIterableWithSize(
                    equalTo(expectedLanguages.size)
                )
            )
        }
    }
}
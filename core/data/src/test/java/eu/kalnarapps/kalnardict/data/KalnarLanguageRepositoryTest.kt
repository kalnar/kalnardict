package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.repositories.KalnarLanguageRepository
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test

class KalnarLanguageRepositoryTest {

    private val repository =
        KalnarLanguageRepository(
            TestLanguageDataDao()
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
            val languageFetch = repository.getLanguageById("non existing")
            assertThat(
                languageFetch,
                instanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }
}
package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.dao.AuxiliaryDataSource
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toLanguageToData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test

@ExperimentalCoroutinesApi
class AuxiliaryDataSourceTest {

    private val testCoroutineScope = TestCoroutineScope()
    private val auxiliaryDataSourceTest = AuxiliaryDataSource(LanguageDaoMock())

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun get_language_by_id() {
        testCoroutineScope.runBlockingTest {
            val frenchFetch = auxiliaryDataSourceTest.getLanguageById(Languages.french.id)
            assertThat(
                frenchFetch,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(frenchFetch is DataOperationResult.Success)
            assertThat(
                frenchFetch.data,
                equalTo(Languages.french.toLanguageToData() as LanguageLogEntryData)
            )
        }
    }

    @Test
    fun get_null_language_by_wrong_id() {
        testCoroutineScope.runBlockingTest {
            assertThat(
                auxiliaryDataSourceTest.getLanguageById("N/A"),
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }

    }
}
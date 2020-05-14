package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Test

class DataSourceTest {

    private val dataSource = DataSource(WordDaoMock())
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun get_dict_entry_for_asztal_by_query() = testCoroutineDispatcher.runBlockingTest {
        val queryResult = dataSource.getDictEntryByQuery("asztal")

        assertThat(
            queryResult,
            not(IsEmptyCollection())
        )

        assertThat(
            queryResult.firstOrNull()?.getTranslation(),
            equalTo("table")
        )

        assertThat(
            dataSource.getDictEntryByQuery("x"),
            IsEmptyCollection()
        )

    }

    @Test
    fun insert_new_word_in_data_source() = testCoroutineDispatcher.runBlockingTest {
        assertThat(
            dataSource.getDictEntryByQuery("szem"),
            IsEmptyCollection()
        )

        dataSource.insertDictEntry(SampleEyeDictEntry)

        assertThat(
            dataSource.getDictEntryByQuery("szem"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.getDictEntryByQuery("szem").firstOrNull()?.getTranslation(),
            equalTo("eye")
        )

    }

    @Test
    fun insert_multiple_entries_in_data_source() = testCoroutineDispatcher.runBlockingTest {
        assertThat(
            dataSource.getDictEntryByQuery("szem"),
            IsEmptyCollection()
        )
        assertThat(
            dataSource.getDictEntryByQuery("doboz"),
            IsEmptyCollection()
        )

        dataSource.insertDictEntries(sampleDictEntries)

        assertThat(
            dataSource.getDictEntryByQuery("szem"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.getDictEntryByQuery("szem").firstOrNull()?.getTranslation(),
            equalTo("eye")
        )
        assertThat(
            dataSource.getDictEntryByQuery("doboz"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.getDictEntryByQuery("doboz").firstOrNull()?.getTranslation(),
            equalTo("box")
        )
    }

}
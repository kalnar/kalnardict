package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDaoAdapter
import eu.kalnarapps.kalnardict.data.model.toNewDictionaryEntry
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.test.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DictionaryLogDaoAdapterTest {

    private val dictLogDaoMock = DictionaryLogDaoMock()
    private val dataSource = DictionaryLogDaoAdapter(
        dictLogDaoMock
    )
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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
    fun list_registered_dictionaries() = testCoroutineRule.runBlockingTest {
        // given there is one dictionary registered
        dictLogDaoMock.insertDictionary(sampleDictionaryLogEntry)

        // when listing the dictionaries
        val testCollector = dataSource.getDictionaries().test(scope = this)

        try {
            // then we have the one sample dictionary log entry as result
            testCollector.assertThat(
                { it.last() },
                IsCollectionWithSize(equalTo(1))
            )
            testCollector.assertThat(
                { it.last()[0].name },
                equalTo(sampleDictionaryLogEntry.dictionaryName)
            )
            testCollector.assertThat(
                { it.last()[0].languageFrom },
                equalTo("hu")
            )
            testCollector.assertThat(
                { it.last()[0].languageTo },
                equalTo("en")
            )
            testCollector.assertThat(
                { it.last()[0].id },
                equalTo(1)
            )
        } finally {
            testCollector.finish()
        }

    }

    @Test
    fun find_no_dictionary_if_there_are_none_registered() = testCoroutineDispatcher
        .runBlockingTest {
            // given there is no dictionary registered

            // when listing the dictionaries
            val testCollector = dataSource.getDictionaries().test(scope = this)

            // then we have an empty list of sample dictionary log entries as result
            try {
                testCollector.assertThat(
                    { it.last() },
                    IsEmptyCollection()
                )
            } finally {
                testCollector.finish()
            }
        }

    @Test
    fun when_inserting_new_dictionary_return_new_id() {
        testCoroutineRule.runBlockingTest {

            val result =
                dataSource.insertDictionary(sampleDictionaryLogEntry.toNewDictionaryEntry())

            assertThat(
                result,
                equalTo(1L)
            )
            val nextResult =
                dataSource.insertDictionary(newSampleDictionaryLogEntry.toNewDictionaryEntry())

            assertThat(
                nextResult,
                equalTo(2L)
            )
        }
    }
}
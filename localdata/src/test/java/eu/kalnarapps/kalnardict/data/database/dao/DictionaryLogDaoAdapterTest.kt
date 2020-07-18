package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDaoAdapter
import eu.kalnarapps.kalnardict.data.model.toNewDictionaryEntry
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
import org.junit.Test

@ExperimentalCoroutinesApi
class DictionaryLogDaoAdapterTest {

    private val dictLogDaoMock = DictionaryLogDaoMock()
    private val dataSource = DictionaryLogDaoAdapter(
        dictLogDaoMock
    )
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
    fun list_registered_dictionaries() = testCoroutineDispatcher.runBlockingTest {
        // given there is one dictionary registered
        dictLogDaoMock.insertDictionary(sampleDictionaryLogEntry)

        // when listing the dictionaries
        val dictionaries = dataSource.getDictionaries()

        // then we have the one sample dictionary log entry as result
        assertThat(
            dictionaries,
            IsCollectionWithSize(equalTo(1))
        )
        assertThat(
            dictionaries[0].name,
            equalTo(sampleDictionaryLogEntry.dictionaryName)
        )
        assertThat(
            dictionaries[0].languageFrom,
            equalTo("hu")
        )
        assertThat(
            dictionaries[0].languageTo,
            equalTo("en")
        )
        assertThat(
            dictionaries[0].id,
            equalTo(1)
        )

    }

    @Test
    fun find_no_dictionary_if_there_are_none_registered() = testCoroutineDispatcher
        .runBlockingTest {
            // given there is no dictionary registered

            // when listing the dictionaries
            val dictionaries = dataSource.getDictionaries()

            // then we have an empty list of sample dictionary log entries as result
            assertThat(
                dictionaries,
                IsEmptyCollection()
            )
        }

    @Test
    fun when_inserting_new_dictionary_return_new_id() {
        testCoroutineDispatcher.runBlockingTest {

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
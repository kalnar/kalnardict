package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDaoAdapter
import eu.kalnarapps.kalnardict.data.model.toNewDictionaryEntry
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Rule
import org.junit.Test


class DictionaryLogDaoAdapterTest {

    private val dictLogDaoMock = DictionaryLogDaoMock()
    private val dataSource = DictionaryLogDaoAdapter(
        dictLogDaoMock
    )

    @Test
    fun list_registered_dictionaries() = runTest {
        // given there is one dictionary registered
        dictLogDaoMock.insertDictionary(sampleDictionaryLogEntry)

        // when listing the dictionaries
        val dictionariesCollected = dataSource.getDictionaries().take(1).toList()

        assertThat(
            dictionariesCollected.last(),
            IsCollectionWithSize(equalTo(1))
        )
        assertThat(
            dictionariesCollected.last()[0].name,
            equalTo(sampleDictionaryLogEntry.dictionaryName)
        )
        assertThat(
            dictionariesCollected.last()[0].languageFrom,
            equalTo("hu")
        )
        assertThat(
            dictionariesCollected.last()[0].languageTo,
            equalTo("en")
        )
        assertThat(
            dictionariesCollected.last()[0].id,
            equalTo(1)
        )
    }

    @Test
    fun find_no_dictionary_if_there_are_none_registered() = runTest {
        // given there is no dictionary registered

        // when listing the dictionaries
        val dictionariesCollected = dataSource.getDictionaries().take(1).toList()

        // then we have an empty list of sample dictionary log entries as result
        assertThat(
            dictionariesCollected.last(),
            IsEmptyCollection()
        )
    }

    @Test
    fun when_inserting_new_dictionary_return_new_id() {
        runTest {

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
package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import eu.kalnarapps.kalnardict.data.mapper.toroom.TranslatedWordMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Test

class DictionaryDataSourceTest {

    private val dictLogDaoMock = DictionaryLogDaoMock()
    private val wordDaoMock = WordDaoMock()
    private val dataSource = DictionaryDataSource(
        wordDaoMock,
        dictLogDaoMock,
        wordInfoMapper = WordInfoMapper(),
        translatedWordMapper = TranslatedWordMapper()
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
    fun get_dict_entry_for_asztal_by_query() = testCoroutineDispatcher.runBlockingTest {
        val queryResult = dataSource.queryWithMatchAnyWhere("asztal")

        assertThat(
            queryResult,
            not(IsEmptyCollection())
        )

        assertThat(
            queryResult.firstOrNull()?.baseForm,
            equalTo(sampleTableInHungarian.baseForm)
        )

        assertThat(
            dataSource.queryWithMatchAnyWhere("x"),
            IsEmptyCollection()
        )

    }

    @Test
    fun insert_new_word_in_data_source() = testCoroutineDispatcher.runBlockingTest {
        assertThat(
            dataSource.queryWithMatchAnyWhere("szem"),
            IsEmptyCollection()
        )

        dataSource.insertDictEntry(SampleEyeWordDataEntry)

        assertThat(
            dataSource.queryWithMatchAnyWhere("szem"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.queryWithMatchAnyWhere("szem").firstOrNull()?.id,
            equalTo(SampleEyeWordDataEntry.id)
        )

    }

    @Test
    fun insert_multiple_entries_in_data_source() = testCoroutineDispatcher.runBlockingTest {
        assertThat(
            dataSource.queryWithMatchAnyWhere("szem"),
            IsEmptyCollection()
        )
        assertThat(
            dataSource.queryWithMatchAnyWhere("doboz"),
            IsEmptyCollection()
        )

        dataSource.insertDictEntries(sampleDictEntries)

        assertThat(
            dataSource.queryWithMatchAnyWhere("szem"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.queryWithMatchAnyWhere("szem").firstOrNull()?.id,
            equalTo(SampleEyeWordDataEntry.id)
        )
        assertThat(
            dataSource.queryWithMatchAnyWhere("doboz"),
            not(IsEmptyCollection())
        )
        assertThat(
            dataSource.queryWithMatchAnyWhere("doboz").firstOrNull()?.id,
            equalTo(SampleBoxWordDataEntry.id)
        )
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

}
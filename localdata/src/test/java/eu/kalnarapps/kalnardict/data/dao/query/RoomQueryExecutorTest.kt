package eu.kalnarapps.kalnardict.data.dao.query

import eu.kalnarapps.kalnardict.data.dao.query.formatter.ExactMatchQueryFormatter
import eu.kalnarapps.kalnardict.data.database.dao.WordDaoMock
import eu.kalnarapps.kalnardict.data.database.dao.sampleTableInHungarian
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RoomQueryExecutorTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val wordDaoMock = WordDaoMock()
    private val mapper = WordInfoMapper()

    // TODO: this should be tested directly on device or some sqlite environment
    //  along with each match mode
    @Test
    fun get_dict_entry_for_asztal_by_exact_query() =
        testCoroutineRule.runBlockingTest {
            val roomQueryExecutor = RoomQueryExecutor(
                wordDaoMock,
                queryFormatter = ExactMatchQueryFormatter(),
                wordInfoMapper = mapper
            )
            val queryResult = roomQueryExecutor.query(
                sampleTableInHungarian.baseForm,
                sampleTableInHungarian.dictionaryId
            )

            assertThat(
                queryResult,
                not(IsEmptyCollection())
            )

            assertThat(
                queryResult.firstOrNull()?.baseForm,
                equalTo(sampleTableInHungarian.baseForm)
            )

            assertThat(
                roomQueryExecutor.query(
                    "x",
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )

        }
}
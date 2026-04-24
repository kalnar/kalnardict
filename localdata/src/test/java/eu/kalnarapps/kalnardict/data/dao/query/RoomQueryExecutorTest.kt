package eu.kalnarapps.kalnardict.data.dao.query

import eu.kalnarapps.kalnardict.data.dao.query.formatter.ExactMatchQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.FuzzyQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchAnyWhereFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchEndQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchStartQueryFormatter
import eu.kalnarapps.kalnardict.data.database.dao.WordDaoMock
import eu.kalnarapps.kalnardict.data.database.dao.sampleTableInHungarian
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Test


class RoomQueryExecutorTest {

    private val wordDaoMock = WordDaoMock()
    private val mapper = WordInfoMapper()

    @Test
    fun get_dict_entry_for_asztal_by_exact_query() =
        runTest {
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
                    sampleTableInHungarian.baseForm.substring(2),
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )
        }

    @Test
    fun get_dict_entry_for_asztal_by_query_with_match_anywhere() =
        runTest {
            val roomQueryExecutor = RoomQueryExecutor(
                wordDaoMock,
                queryFormatter = MatchAnyWhereFormatter(),
                wordInfoMapper = mapper
            )
            val queryResult = roomQueryExecutor.query(
                sampleTableInHungarian.baseForm.substring(
                    2, sampleTableInHungarian.baseForm.length - 1
                ),
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
                    sampleTableInHungarian.baseForm.substring(2) + "x",
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )
        }

    @Test
    fun get_dict_entry_for_asztal_by_query_with_match_ending() =
        runTest {
            val roomQueryExecutor = RoomQueryExecutor(
                wordDaoMock,
                queryFormatter = MatchEndQueryFormatter(),
                wordInfoMapper = mapper
            )
            val queryResult = roomQueryExecutor.query(
                sampleTableInHungarian.baseForm.substring(
                    2, sampleTableInHungarian.baseForm.length
                ),
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
                    sampleTableInHungarian.baseForm.substring(
                        0, sampleTableInHungarian.baseForm.length - 2
                    ),
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )
        }

    @Test
    fun get_dict_entry_for_asztal_by_query_with_match_beginning() =
        runTest {
            val roomQueryExecutor = RoomQueryExecutor(
                wordDaoMock,
                queryFormatter = MatchStartQueryFormatter(),
                wordInfoMapper = mapper
            )
            val queryResult = roomQueryExecutor.query(
                sampleTableInHungarian.baseForm.substring(
                    0, sampleTableInHungarian.baseForm.length - 3
                ),
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
                    sampleTableInHungarian.baseForm.substring(
                        2, sampleTableInHungarian.baseForm.length
                    ),
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )
        }

    @Test
    fun get_dict_entry_for_asztal_by_query_with_match_fuzzy() =
        runTest {
            val roomQueryExecutor = RoomQueryExecutor(
                wordDaoMock,
                queryFormatter = FuzzyQueryFormatter(),
                wordInfoMapper = mapper
            )
            val queryResult = roomQueryExecutor.query(
                with(sampleTableInHungarian.baseForm) {
                    substring(1, 2) + substring(length - 1, length)
                },
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
                    with(sampleTableInHungarian.baseForm) {
                        substring(length - 1, length) + substring(1, 2)
                    },
                    sampleTableInHungarian.dictionaryId
                ),
                IsEmptyCollection()
            )
        }
}
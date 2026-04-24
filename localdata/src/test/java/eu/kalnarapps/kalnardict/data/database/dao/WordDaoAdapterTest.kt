package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.dao.WordDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.query.RoomQueryExecutor
import eu.kalnarapps.kalnardict.data.dao.query.formatter.ExactMatchQueryFormatter
import eu.kalnarapps.kalnardict.data.mapper.todata.WordInfoMapper
import eu.kalnarapps.kalnardict.data.mapper.toroom.TranslatedWordMapper
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsInstanceOf
import org.junit.Test


class WordDaoAdapterTest {

    private val wordDaoMock = WordDaoMock()
    private val dataSource = WordDaoAdapter(
        wordDaoMock,
        translatedWordMapper = TranslatedWordMapper()
    )
    private val roomQueryExecutor = RoomQueryExecutor(
        wordDaoMock,
        queryFormatter = ExactMatchQueryFormatter(),
        wordInfoMapper = WordInfoMapper()
    )
    @Test
    fun insert_new_word_in_data_source() = runTest {
        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ),
            IsEmptyCollection()
        )

        dataSource.insertDictEntry(SampleEyeWordDataEntry)

        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ),
            not(IsEmptyCollection())
        )
        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ).firstOrNull()?.baseForm,
            equalTo(SampleEyeWordDataEntry.baseForm)
        )
    }

    @Test
    fun insert_multiple_entries_in_data_source() = runTest {
        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ),
            IsEmptyCollection()
        )
        assertThat(
            roomQueryExecutor.query(
                "doboz",
                SampleBoxWordDataEntry.dictionaryId
            ),
            IsEmptyCollection()
        )

        dataSource.insertDictEntries(sampleDictEntries)

        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ),
            not(IsEmptyCollection())
        )
        assertThat(
            roomQueryExecutor.query(
                "szem",
                SampleEyeWordDataEntry.dictionaryId
            ).firstOrNull()?.baseForm,
            equalTo(SampleEyeWordDataEntry.baseForm)
        )
        assertThat(
            roomQueryExecutor.query(
                "doboz",
                SampleBoxWordDataEntry.dictionaryId
            ),
            not(IsEmptyCollection())
        )
        assertThat(
            roomQueryExecutor.query(
                "doboz",
                SampleBoxWordDataEntry.dictionaryId
            ).firstOrNull()?.baseForm,
            equalTo(SampleBoxWordDataEntry.baseForm)
        )
    }

    @Test
    fun when_getting_translation_with_valid_ids_return_translation() {
        runTest {
            val translationInfo = dataSource.getTranslationByWordAndDictionaryId(
                sampleTableInHungarian.id,
                sampleTableInHungarian.dictionaryId
            )

            assertThat(
                translationInfo,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(translationInfo is DataOperationResult.Success)
            assertThat(
                translationInfo.data,
                equalTo(sampleTableInHungarian.translation)
            )
        }
    }

    @Test
    fun when_getting_translation_with_invalid_ids_return_failure() {
        runTest {
            val translationInfo = dataSource.getTranslationByWordAndDictionaryId(
                sampleTableInHungarian.id,
                DICTIONARY_ID_SECOND
            )

            assertThat(
                translationInfo,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }
}
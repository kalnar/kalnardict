package eu.kalnarapps.kalnardict.data.dao.dictionary

import eu.kalnarapps.kalnardict.data.dao.DaoStubs
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class DisplayTypeDaoAdapterTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val displayTypePreferencesDao =
        mock(DisplayTypePreferencesDao::class.java)

    @Test
    fun return_display_type_data_when_getting_with_correct_id() {

        testCoroutineRule.runBlockingTest {

            val dictionaryId = DaoStubs.Dictionaries.frenchToFrenchDicitonaryId
            val expectedDisplayTypeString = DaoStubs.DisplayTypes.HTML_DISPLAY_TYPE
            `when`(displayTypePreferencesDao.getDisplayTypeForDictionary(dictionaryId))
                .thenReturn(flowOf(expectedDisplayTypeString))

            // when
            val dao = DisplayTypeDaoAdapter(
                displayTypePreferencesDao = displayTypePreferencesDao
            )
            val displayTypeData = dao.displayTypeForDictionaryById(dictionaryId)

            val testCollector = displayTypeData.test(scope = this)
            try {
                testCollector.assertThatLastValue(
                    equalTo(DaoStubs.DisplayTypes.frenchToFrenchDictionaryDisplayType)
                )

            } finally {
                testCollector.finish()
            }

        }
    }
}
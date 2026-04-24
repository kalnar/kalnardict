package eu.kalnarapps.kalnardict.data.dao.dictionary

import eu.kalnarapps.kalnardict.data.dao.DaoStubs
import eu.kalnarapps.kalnardict.data.entities.DictionaryDisplayTypeData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsIterableContaining
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class DisplayTypeDaoAdapterTest {

    private val displayTypePreferencesDao =
        mock(DisplayTypePreferencesDao::class.java)
    private val supportedTypesDao =
        mock(SupportedDisplayTypesDao::class.java)

    @Test
    fun return_display_type_data_when_getting_with_correct_id() {
        runTest {

            val dictionaryId = DaoStubs.Dictionaries.frenchToFrenchDicitonaryId
            val expectedDisplayTypeString = DaoStubs.DisplayTypes.HTML_DISPLAY_TYPE
            `when`(displayTypePreferencesDao.getDisplayTypeForDictionaryFlow(dictionaryId))
                .thenReturn(flowOf(expectedDisplayTypeString))

            // when
            val dao = DisplayTypeDaoAdapter(
                displayTypePreferencesDao = displayTypePreferencesDao,
                supportedTypesDao = supportedTypesDao
            )
            val displayTypeData = dao.displayTypeForDictionaryByIdFlow(dictionaryId).toList()

            assertThat(
                displayTypeData.last(),
                equalTo(DaoStubs.DisplayTypes.frenchToFrenchDictionaryDisplayType)
            )
        }
    }

    @Test
    fun return_supported_display_types_when_getting_with_correct_id() {
        runTest {

            val dictionaryId = DaoStubs.Dictionaries.frenchToFrenchDicitonaryId
            val supportedDisplayTypeString = DaoStubs.DisplayTypes.HTML_DISPLAY_TYPE
            val supportedDisplayTypeStrings = listOf(
                DictionaryDisplayTypeData(
                    DaoStubs.DisplayTypes.HTML_DISPLAY_TYPE
                )
            )
            `when`(supportedTypesDao.getSupportedDisplayTypesForDictionaryWithId(dictionaryId))
                .thenReturn(flowOf(supportedDisplayTypeStrings))

            // when
            val dao = DisplayTypeDaoAdapter(
                displayTypePreferencesDao = displayTypePreferencesDao,
                supportedTypesDao = supportedTypesDao
            )
            val displayTypesData = dao.supportedDisplayTypesForDictionaryById(dictionaryId).toList()

            assertThat(
                displayTypesData.last(),
                IsIterableContaining(
                    equalTo(
                        DictionaryDisplayTypeData(
                            supportedDisplayTypeString
                        )
                    )
                )
            )
        }
    }
}
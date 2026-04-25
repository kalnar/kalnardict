package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.dao.LanguageDaoAdapter
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.LanguageRoomMapper
import eu.kalnarapps.kalnardict.data.mapper.toLanguageToData
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.collection.IsIterableWithSize
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsIterableContaining
import org.junit.Test


class LanguageDaoAdapterTest {

    private val auxiliaryDataSourceTest =
        LanguageDaoAdapter(LanguageDaoMock(), LanguageRoomMapper())

    @Test
    fun get_language_by_id() {
        runTest {
            val frenchFetch = auxiliaryDataSourceTest.getLanguageById(Languages.french.id)
            assertThat(
                frenchFetch,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(frenchFetch is DataOperationResult.Success)
            assertThat(
                frenchFetch.data,
                equalTo(Languages.french.toLanguageToData() as LanguageLogEntryData)
            )
        }
    }

    @Test
    fun get_null_language_by_wrong_id() {
        runTest {
            assertThat(
                auxiliaryDataSourceTest.getLanguageById("N/A"),
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    @Test
    fun get_languages_when_available() {
        runTest {
            val auxiliaryDataSourceTest = LanguageDaoAdapter(
                LanguageDaoMock(ArrayList(Languages.frenchAndEnglish)),
                LanguageRoomMapper()
            )
            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                IsIterableContainingInAnyOrder(
                    Languages.frenchAndEnglish.map {
                        equalTo<LanguageLogEntryData>(it.toLanguageToData())
                    }
                )
            )
        }
    }

    @Test
    fun get_empty_list_of_languages_when_none_available() {
        runTest {
            val auxiliaryDataSourceTest = LanguageDaoAdapter(
                LanguageDaoMock(ArrayList(emptyList())),
                LanguageRoomMapper()
            )
            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                IsEmptyCollection()
            )
        }
    }


    @Test
    fun add_new_language_with_unique_id() {
        runTest {
            val auxiliaryDataSourceTest = LanguageDaoAdapter(
                LanguageDaoMock(ArrayList(Languages.frenchAndEnglish)),
                LanguageRoomMapper()
            )
            val languageToAdd = LocalDataLanguages.russian
            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                not(
                    IsIterableContaining(equalTo<LanguageLogEntryData>(LocalDataLanguages.russian))
                )
            )

            auxiliaryDataSourceTest.addLanguage(languageToAdd)

            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                IsIterableContaining(
                    equalTo<LanguageLogEntryData>(languageToAdd)
                )
            )
        }
    }

    @Test
    fun attempt_add_new_language_with_non_unique_id_and_return_fail() {
        runTest {
            val languages = Languages.frenchAndEnglish
            val auxiliaryDataSourceTest = LanguageDaoAdapter(
                LanguageDaoMock(ArrayList(languages)),
                LanguageRoomMapper()
            )
            val languageToAdd = LocalDataLanguages.french
            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                IsIterableWithSize(equalTo(languages.size))
            )

            val result = auxiliaryDataSourceTest.addLanguage(languageToAdd)

            assertThat(
                result,
                IsInstanceOf(OperationResult.Failure::class.java)
            )

            assertThat(
                auxiliaryDataSourceTest.getLanguages(),
                IsIterableWithSize(equalTo(languages.size))
            )
        }
    }
}
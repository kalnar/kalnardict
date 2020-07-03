package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.TestDictDao
import eu.kalnarapps.kalnardict.data.TestLanguageDataDao
import eu.kalnarapps.kalnardict.data.mock.MockConfigurationDao
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppConfigRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initial_dictionary_is_not_set() {
        testCoroutineRule.runBlockingTest {

            val repository = AppConfigRepository(
                configurationDao = MockConfigurationDao(),
                dictDao = TestDictDao(),
                languageDataDao = TestLanguageDataDao()
            )
            val language = repository.getCurrentDictionary()
            assertThat(
                language,
                IsInstanceOf(CurrentDictionary.DictionaryNotSet::class.java)
            )

        }
    }

    @Test
    fun update_current_dictionary() {
        testCoroutineRule.runBlockingTest {
            val repository = AppConfigRepository(
                configurationDao = MockConfigurationDao(),
                dictDao = TestDictDao(),
                languageDataDao = TestLanguageDataDao()
            )

            repository.updateCurrentDictionary(
                Stubs.Dictionaries.frenchEnglishDictionary
            )

            assertThat(
                (repository.getCurrentDictionary() as CurrentDictionary.SetDictionary).dictionary,
                equalTo(Stubs.Dictionaries.frenchEnglishDictionary)
            )

        }
    }
}
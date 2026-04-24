package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mock.MockConfigurationDataSource
import eu.kalnarapps.kalnardict.data.mock.MockDictionaryDataSource
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataSource
import eu.kalnarapps.kalnardict.data.repositories.configuration.AppConfigRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test


class AppConfigRepositoryTest {

    @Test
    fun initial_dictionary_is_not_set() {
        runTest {
            val repository =
                AppConfigRepository(
                    configurationDataSource = MockConfigurationDataSource(),
                    dictionaryDataSource = MockDictionaryDataSource(),
                    languageDataSource = MockLanguageDataSource()
                )

            val currentDictionaryCollectedFlow = repository.getCurrentDictionary().toList()

            assertThat(
                currentDictionaryCollectedFlow.last(),
                equalTo(
                    CurrentDictionary.DictionaryNotSet
                )
            )
        }
    }

    @Test
    fun update_current_dictionary() {
        runTest {
            val repository =
                AppConfigRepository(
                    configurationDataSource = MockConfigurationDataSource(),
                    dictionaryDataSource = MockDictionaryDataSource(),
                    languageDataSource = MockLanguageDataSource()
                )

            repository.updateCurrentDictionary(
                Stubs.Dictionaries.frenchEnglishDictionary
            )

            val currentDictionary = repository.getCurrentDictionary().toList()
            assertThat(
                currentDictionary.last(),
                equalTo(
                    CurrentDictionary.SetDictionary(
                        Stubs.Dictionaries.frenchEnglishDictionary
                    )
                )
            )
        }
    }
}
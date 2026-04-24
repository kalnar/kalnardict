package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mapper.DictionaryMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mock.MockDictionaryDataSource
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataSource
import eu.kalnarapps.kalnardict.data.mock.MockQueryExecutorProvider
import eu.kalnarapps.kalnardict.data.mock.MockWordDataSource
import eu.kalnarapps.kalnardict.data.mock.TestExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.mock.model.MockTranslatedWordEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Test

class RepositoryGetEntriesTest {

    private val languageMapper = LanguageDataMapper()
    private val mockDb: ArrayList<TranslatedWordDataEntry> = ArrayList<TranslatedWordDataEntry>(
        listOf(
            MockTranslatedWordEntry(
                id = 1,
                baseForm = Stubs.Words.wordTakeBaseForm,
                alternativeBaseForm = Stubs.Words.wordTakeBaseForm,
                dictionaryId = Stubs.Dictionaries.englishToFrenchDictionaryId,
                translation = Stubs.Words.wordTakeFrenchTranslationText
            )
        )
    )
    private val repository =
        Repository(
            MockWordDataSource(mockDb),
            MockQueryExecutorProvider(mockDb),
            MockDictionaryDataSource(),
            TestExternalDatabaseHandler(),
            DictionaryMapper(
                KalnarLanguageRepository(
                    MockLanguageDataSource(),
                    languageMapper,
                    languageMapper
                )
            )
        )

    @Test
    fun when_using_match_exact_mode_return_only_if_match_is_exact() {
        // given a correct path of a valid external db
        runTest {
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm,
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_EXACT
                    )
                ),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(1),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_EXACT
                    )
                ),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun when_using_match_beginning_mode_return_only_if_starting_with() {
        // given a correct path of a valid external db
        runTest {
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(
                            0,
                            Stubs.Words.wordTakeBaseForm.length - 2
                        ),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_BEGINNING
                    )
                ),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(1),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_BEGINNING
                    )
                ),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun when_using_match_end_mode_return_only_if_ending_with() {
        // given a correct path of a valid external db
        runTest {
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(
                            2,
                            Stubs.Words.wordTakeBaseForm.length
                        ),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_END
                    )
                ),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(
                            0,
                            Stubs.Words.wordTakeBaseForm.length - 2
                        ),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_END
                    )
                ),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun when_using_match_anywhere_mode_return_only_if_contains_string() {
        // given a correct path of a valid external db
        runTest {
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(
                            1,
                            Stubs.Words.wordTakeBaseForm.length - 1
                        ),
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_ANYWHERE
                    )
                ),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.substring(
                            0,
                            Stubs.Words.wordTakeBaseForm.length - 2
                        ) + "x",
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_ANYWHERE
                    )
                ),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun when_using_match_fuzzy_mode_return_only_if_contains_characters_in_order() {
        // given a correct path of a valid external db
        runTest {
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.run {
                            substring(1, 2) + substring(length - 1, length)
                        },
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_FUZZY
                    )
                ),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.getEntriesByQuery(
                    DictQuery(
                        queryString = Stubs.Words.wordTakeBaseForm.run {
                            substring(length - 1, length) + substring(1, 2)
                        },
                        dictionary = Stubs.Dictionaries.englishToFrenchDictionary,
                        queryMode = QueryMode.MATCH_FUZZY
                    )
                ),
                IsEmptyCollection()
            )
        }
    }
}
package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mapper.DictionaryMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mock.MockDictionaryDataSource
import eu.kalnarapps.kalnardict.data.mock.MockDictionaryMapper
import eu.kalnarapps.kalnardict.data.mock.MockEmptyWordDataSource
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataSource
import eu.kalnarapps.kalnardict.data.mock.MockQueryExecutorProvider
import eu.kalnarapps.kalnardict.data.mock.MockWordDataSource
import eu.kalnarapps.kalnardict.data.mock.TestExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.sampleExternalDbTable
import eu.kalnarapps.kalnardict.data.sampleQueryNewWord
import eu.kalnarapps.kalnardict.data.validExternalResource
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsIterableContaining
import org.hamcrest.text.IsEqualIgnoringCase
import org.junit.Test


class RepositoryTest {

    private val languageMapper = LanguageDataMapper()
    private val mockDb: ArrayList<TranslatedWordDataEntry> = ArrayList<TranslatedWordDataEntry>()
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
    fun import_one_table_from_valid_external_db() {
        runTest {
            // given a correct path of a valid external db
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )
            var dictionaries: List<Dictionary> = emptyList()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                repository.readRegisteredDictionaries().collect {
                    dictionaries = it
                }
            }

            assertThat(
                dictionaries,
                IsCollectionWithSize(
                    equalTo(
                        Stubs.Dictionaries.newDictionary.id - 1
                    )
                )
            )

            assertThat(
                dictionaries,
                not(
                    IsIterableContaining(
                        HasPropertyWithValue<Int>(
                            "id",
                            equalTo(Stubs.Dictionaries.newDictionary.id)
                        )
                    )
                )
            )

            // when
            val savingName = "sampleExternalDbTable"
            val importResult = repository.importTableFromDb(
                ImportJob(
                    sampleExternalDbTable,
                    validExternalResource,
                    savingName,
                    1
                )
            ).toList()

            val firstResult = importResult.first()
            assertThat(
                firstResult,
                instanceOf(DataOperationResult.Success::class.java)
            )
            check(firstResult is DataOperationResult.Success)
            assertThat(
                firstResult.data,
                equalTo(Stubs.Db.progressOneItem)
            )

            val secondResult = importResult[1]
            assertThat(
                secondResult,
                instanceOf(DataOperationResult.Success::class.java)
            )
            check(secondResult is DataOperationResult.Success)
            assertThat(
                secondResult.data,
                equalTo(Stubs.Db.progressTwoItem)
            )


            // then

            assertThat(
                dictionaries,
                IsIterableContaining(
                    HasPropertyWithValue<Int>(
                        "id",
                        equalTo(Stubs.Dictionaries.newDictionary.id)
                    )
                )
            )

            assertThat(
                dictionaries,
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "description",
                        equalTo(
                            savingName
                        )
                    )
                )
            )

            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                not(IsEmptyCollection())
            )
        }
    }

    @Test
    fun fail_to_import_invalid_external_db() {
        // given a correct path of a valid external db
        val externalDbPath = "mockInvalidDbPath"
        runTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )

            // when
            val importResult = repository.importTableFromDb(
                ImportJob(
                    sampleExternalDbTable,
                    ExternalDatabase.LocalFile(localPath = externalDbPath),
                    batchSize = 1
                )
            ).toList()

            assertThat(
                importResult,
                IsCollectionWithSize(equalTo(1))
            )

            val firstResult = importResult.first()
            assertThat(
                firstResult,
                instanceOf(DataOperationResult.Failure::class.java)
            )

            // then
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )

        }
    }

    @Test
    fun read_registered_dictionaries_and_find_none() {

        val repository = Repository(
            MockEmptyWordDataSource(),
            MockQueryExecutorProvider(),
            MockDictionaryDataSource(ArrayList()),
            TestExternalDatabaseHandler(),
            MockDictionaryMapper()
        )

        runTest {
            val dictionaries = repository.readRegisteredDictionaries().take(1).toList()
            assertThat(
                dictionaries.last(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun read_registered_dictionaries_and_return_found_ones() {

        runTest {
            val dictionaries = repository.readRegisteredDictionaries().take(1).toList()
            assertThat(
                dictionaries.last() as Collection<Dictionary>,
                not(IsEmptyCollection())
            )
            assertThat(
                dictionaries.last()[0].id,
                equalTo(1)
            )
            assertThat(
                dictionaries.last()[0].languageFrom.code,
                equalTo("hu")
            )
            assertThat(
                dictionaries.last()[0].languageFrom.name,
                IsEqualIgnoringCase("magyar")
            )
        }
    }

    @Test
    fun read_meta_info_of_external_db() {
        runTest {
            val metaInfoFetch = repository.readMetaInfoFromExternalDb(
                Stubs.Db.validExternalDatabase
            )

            assertThat(
                metaInfoFetch,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(metaInfoFetch is DataOperationResult.Success)
            assertThat(
                metaInfoFetch.data,
                IsIterableContainingInAnyOrder(
                    listOf(
                        equalTo(Stubs.MetaInfoOnDb.table1),
                        equalTo(Stubs.MetaInfoOnDb.table2)
                    )
                )
            )
        }
    }

    @Test
    fun attempt_read_meta_info_of_invalid_external_db() {
        runTest {
            val metaInfoFetch = repository.readMetaInfoFromExternalDb(
                Stubs.Db.invalidExternalDatabase
            )

            assertThat(
                metaInfoFetch,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    @Test
    fun return_translation_when_called_with_available_word_id() {
        runTest {
            test_translation_when_called_with_available_word_id(
                givenWordList = listOf(
                    Stubs.Words.translatedWordPrendre,
                    Stubs.Words.translatedWordTake
                ),
                wordId = Stubs.Words.wordTakeId,
                currentDictionary = CurrentDictionary.SetDictionary(
                    Stubs.Dictionaries.englishToFrenchDictionary
                ),
                expectedTranslation = Stubs.Words.wordTakeFrenchTranslationText
            )
            test_translation_when_called_with_available_word_id(
                givenWordList = listOf(
                    Stubs.Words.translatedWordPrendre,
                    Stubs.Words.translatedWordTake
                ),
                wordId = Stubs.Words.wordPrendreId,
                currentDictionary = CurrentDictionary.SetDictionary(
                    Stubs.Dictionaries.frenchToFrenchDictionary
                ),
                expectedTranslation = Stubs.Words.wordPrendreFrenchTranslationText
            )
        }
    }

    @Test
    fun return_operation_failure_when_getting_translation_with_wrong_id() {
        runTest {
            val repository = Repository(
                MockWordDataSource(
                    ArrayList(
                        listOf(
                            Stubs.Words.translatedWordPrendre,
                            Stubs.Words.translatedWordTake
                        )
                    )
                ),
                MockQueryExecutorProvider(),
                MockDictionaryDataSource(),
                TestExternalDatabaseHandler(),
                MockDictionaryMapper()
            )
            // when
            val result = repository.getTranslationById(
                Stubs.Words.wordTakeId,
                Stubs.Dictionaries.frenchToFrenchDictionary.id
            )

            // then
            assertThat(
                result,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    private suspend fun test_translation_when_called_with_available_word_id(
        givenWordList: List<TranslatedWordDataEntry>,
        wordId: Int,
        currentDictionary: CurrentDictionary.SetDictionary,
        expectedTranslation: String
    ) {
        val repository = Repository(
            MockWordDataSource(ArrayList(givenWordList)),
            MockQueryExecutorProvider(),
            MockDictionaryDataSource(),
            TestExternalDatabaseHandler(),
            MockDictionaryMapper()
        )
        // when
        val result = repository.getTranslationById(wordId, currentDictionary.dictionary.id)

        // then
        assertThat(
            result,
            IsInstanceOf(DataOperationResult.Success::class.java)
        )
        check(result is DataOperationResult.Success<String>)
        assertThat(
            result.data,
            equalTo(expectedTranslation)
        )
    }
}
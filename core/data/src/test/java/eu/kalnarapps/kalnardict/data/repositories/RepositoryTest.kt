package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mapper.DictionaryMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mock.MockDictDao
import eu.kalnarapps.kalnardict.data.mock.MockDictionaryMapper
import eu.kalnarapps.kalnardict.data.mock.MockEmptyDictDao
import eu.kalnarapps.kalnardict.data.mock.MockLanguageDataDao
import eu.kalnarapps.kalnardict.data.mock.TestExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.sampleExternalDbTable
import eu.kalnarapps.kalnardict.data.sampleQueryNewWord
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.validExternalResource
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsIterableContaining
import org.hamcrest.text.IsEqualIgnoringCase
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class RepositoryTest {

    private val languageMapper = LanguageDataMapper()
    private val repository =
        Repository(
            MockDictDao(),
            TestExternalDatabaseHandler(),
            DictionaryMapper(
                KalnarLanguageRepository(
                    MockLanguageDataDao(),
                    languageMapper,
                    languageMapper
                )
            )
        )

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun import_one_tables_from_valid_external_db() {
        // given a correct path of a valid external db
        testCoroutineRule.runBlockingTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )
            assertThat(
                repository.readRegisteredDictionaries(),
                IsCollectionWithSize(
                    equalTo(
                        Stubs.Dictionaries.newDictionary.id - 1
                    )
                )
            )
            assertThat(
                repository.readRegisteredDictionaries(),
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
            val importResult = repository.importTableFromDb(
                ImportJob(
                    sampleExternalDbTable,
                    validExternalResource,
                    "sampleExternalDbTable",
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


            assertThat(
                repository.readRegisteredDictionaries(),
                IsIterableContaining(
                    HasPropertyWithValue<Int>(
                        "id",
                        equalTo(Stubs.Dictionaries.newDictionary.id)
                    )
                )
            )

            // then
            assertThat(
                repository.readRegisteredDictionaries(),
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "description",
                        equalTo(
                            sampleExternalDbTable.name
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
        testCoroutineRule.runBlockingTest {
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
            MockEmptyDictDao(),
            TestExternalDatabaseHandler(),
            MockDictionaryMapper()
        )

        testCoroutineRule.runBlockingTest {
            assertThat(
                repository.readRegisteredDictionaries(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun read_registered_dictionaries_and_return_found_ones() {

        testCoroutineRule.runBlockingTest {
            val dictionaries = repository.readRegisteredDictionaries()
            assertThat(
                dictionaries,
                not(IsEmptyCollection())
            )
            assertThat(
                dictionaries[0].id,
                equalTo(1)
            )
            assertThat(
                dictionaries[0].languageFrom.code,
                equalTo("hu")
            )
            assertThat(
                dictionaries[0].languageFrom.name,
                IsEqualIgnoringCase("magyar")
            )
        }
    }

    @Test
    fun read_meta_info_of_external_db() {
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
            val repository = Repository(
                MockDictDao(
                    ArrayList(
                        listOf(
                            Stubs.Words.translatedWordPrendre,
                            Stubs.Words.translatedWordTake
                        )
                    )
                ),
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
            MockDictDao(ArrayList(givenWordList)),
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
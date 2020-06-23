package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsIterableContaining
import org.hamcrest.text.IsEqualIgnoringCase
import org.junit.After
import org.junit.Test
import java.net.URI


class RepositoryTest {

    private val repository = Repository(TestDictDao(), TestExternalDatabaseHandler())
    private val testCoroutineScope = TestCoroutineScope()

    @Test
    fun import_one_tables_from_valid_external_db() {
        // given a correct path of a valid external db
        testCoroutineScope.runBlockingTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )
            assertThat(
                repository.readRegisteredDictionaries(),
                not(
                    IsIterableContaining(
                        HasPropertyWithValue<Int>(
                            "id",
                            equalTo(2)
                        )
                    )
                )
            )

            // when
            val importResult = repository.importTableFromDb(
                ImportJob(
                    sampleExternalDbTable,
                    validExternalResource,
                    "sampleExternalDbTable"
                )
            )
            assertThat(
                importResult,
                instanceOf(OperationResult.Success::class.java)
            )
            assertThat(
                repository.readRegisteredDictionaries(),
                IsIterableContaining(
                    HasPropertyWithValue<Int>(
                        "id",
                        equalTo(2)
                    )
                )
            )

            // then
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                not(IsEmptyCollection())
            )
            assertThat(
                repository.readRegisteredDictionaries(),
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "description",
                        containsString(
                            "en_dictionary"
                        )
                    )
                )
            )

        }
    }

    @Test
    fun fail_to_import_invalid_external_db() {
        // given a correct path of a valid external db
        val externalDbPath = "mockInvalidDbPath"
        testCoroutineScope.runBlockingTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )

            // when
            val importResult = repository.importTableFromDb(
                ImportJob(
                    sampleExternalDbTable,
                    ExternalDatabase.LocalFile(uri = URI(externalDbPath))
                )
            )
            assertThat(
                importResult,
                instanceOf(OperationResult.Failure::class.java)
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

        val repository = Repository(TestEmptyDictDao(), TestExternalDatabaseHandler())

        testCoroutineScope.runBlockingTest {
            assertThat(
                repository.readRegisteredDictionaries(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun read_registered_dictionaries_and_return_found_ones() {

        testCoroutineScope.runBlockingTest {
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

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }

}
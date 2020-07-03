package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.repositories.Repository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsIterableContaining
import org.hamcrest.text.IsEqualIgnoringCase
import org.junit.After
import org.junit.Test
import java.net.URI


@ExperimentalCoroutinesApi
class RepositoryTest {

    private val repository =
        Repository(
            TestDictDao(),
            TestExternalDatabaseHandler()
        )
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
                        equalTo(Stubs.Dictionaries.newDictionary.id)
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
                        equalTo(
                            sampleExternalDbTable.name
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

        val repository = Repository(
            TestEmptyDictDao(),
            TestExternalDatabaseHandler()
        )

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

    @Test
    fun read_meta_info_of_external_db() {
        testCoroutineScope.runBlockingTest {
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
        testCoroutineScope.runBlockingTest {
            val metaInfoFetch = repository.readMetaInfoFromExternalDb(
                Stubs.Db.invalidExternalDatabase
            )

            assertThat(
                metaInfoFetch,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }

}
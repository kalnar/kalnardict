package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.operations.OperationResult
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Test
import java.net.URI


class RepositoryTest {

    private val repository = Repository(TestDictDao(), TestExternalDatabaseHandler())
    private val testCoroutineScope = TestCoroutineScope()

    @Test
    fun import_all_tables_from_valid_external_db() {
        // given a correct path of a valid external db
        testCoroutineScope.runBlockingTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )

            // when
            val importResult = repository.importTablesFromDb(
                ImportJob(
                    listOf(sampleExternalDbTable),
                    validExternalResource
                )
            )
            assertThat(
                importResult,
                instanceOf(OperationResult.Success::class.java)
            )

            // then
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
        testCoroutineScope.runBlockingTest {
            assertThat(
                repository.getEntriesByQuery(sampleQueryNewWord),
                IsEmptyCollection()
            )

            // when
            val importResult = repository.importTablesFromDb(
                ImportJob(
                    listOf(sampleExternalDbTable),
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

}
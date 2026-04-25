package eu.kalnarapps.kalnardict.data.repositories.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.datasources.database.DatabaseMetaDataSource
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class DefaultDatabaseRepositoryTest {

    private val databaseMetaDataSource: DatabaseMetaDataSource = mock()
    private val externalDatabaseGateway: ExternalDatabaseGateway = mock()

    @Test
    fun `Given a valid database path, when creating external db, then succeed`() {
        runTest {

            // given we have a valid database path
            val givenDbPath = "mock://path/dir/db.sql"

            externalDatabaseGateway.stub {
                onBlocking { createDatabase(givenDbPath) }.thenReturn(OperationResult.Success)
            }

            val repository = DefaultDatabaseRepository(
                databaseMetaDataSource = databaseMetaDataSource,
                externalDatabaseGateway = externalDatabaseGateway
            )

            val createDbResult = repository.createDatabase(givenDbPath)

            assertThat(
                createDbResult,
                equalTo(OperationResult.Success)
            )
            verify(databaseMetaDataSource).addDatabase(givenDbPath)
        }
    }

    @Test
    fun `Given an invalid database path, when creating external db, then fail`() {
        runTest {

            // given we have a valid database path
            val givenDbPath = "mock://path/to/an/already/created/db.sql"

            val givenError = OperationResult.Failure(
                errorMessage = "$givenDbPath already exists"
            )
            externalDatabaseGateway.stub {
                onBlocking { createDatabase(givenDbPath) }.thenReturn(givenError)
            }

            val repository = DefaultDatabaseRepository(
                databaseMetaDataSource = databaseMetaDataSource,
                externalDatabaseGateway = externalDatabaseGateway
            )

            val createDbResult = repository.createDatabase(givenDbPath)

            assertThat(
                createDbResult,
                equalTo(
                    OperationResult.Failure(
                        errorMessage = "ExternalDatabaseGateway::createDatabase failed with $givenDbPath",
                        cause = givenError
                    )
                )
            )
            verify(databaseMetaDataSource, never()).addDatabase(givenDbPath)
        }
    }
}
package eu.kalnarapps.kalnardict.interactors.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DatabaseRepository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class CreateExternalTableTest {

    private val databaseRepository: DatabaseRepository = mock(DatabaseRepository::class.java)
    private val tableRepository: DatabaseRepository.TableRepository =
        mock(DatabaseRepository.TableRepository::class.java)

    @Test
    fun `Given database does not exist, when creating external table, then succeed`() {
        runTest {

            // given we have a valid mock database info path and creating database doesn't fail
            val givenPath = "mock://path/dir/db.sql"
            val givenJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenPath),
                table = ExternalDatabaseTable(
                    name = "givenTableName",
                    languageFrom = "languageFrom",
                    languageTo = "languageTo"
                ),
                size = 5000
            )
            databaseRepository.stub {
                on { createDatabase(givenPath) }.doReturn(OperationResult.Success)
            }

            tableRepository.stub {
                on { createTable(givenJobInfo) }.doReturn(OperationResult.Success)
            }

            val useCase = CreateExternalTable(
                databaseRepository = databaseRepository,
                tableRepository = tableRepository
            )

            val actualExternalTableCreationResult = useCase.invoke(givenJobInfo)

            assertThat(
                actualExternalTableCreationResult,
                equalTo(OperationResult.Success)
            )
        }
    }

    @Test
    fun `Given database does not exist but table is already created, when creating external table, then fail`() {
        runTest {

            // given we have a valid mock database info path and creating database doesn't fail
            val givenPath = "mock://path/dir/db.sql"
            val givenJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenPath),
                table = ExternalDatabaseTable(
                    name = "givenTableName",
                    languageFrom = "languageFrom",
                    languageTo = "languageTo"
                ),
                size = 5000
            )
            databaseRepository.stub {
                on { createDatabase(givenPath) }.doReturn(OperationResult.Success)
            }

            val givenError = OperationResult.Failure("table already exists")
            tableRepository.stub {
                on { createTable(givenJobInfo) }.doReturn(givenError)
            }

            val useCase = CreateExternalTable(
                databaseRepository = databaseRepository,
                tableRepository = tableRepository
            )

            val actualExternalTableCreationResult = useCase.invoke(givenJobInfo)

            assertThat(
                actualExternalTableCreationResult,
                equalTo(
                    OperationResult.Failure(
                        errorMessage = "TableRepository::createTable failed with argument: $givenJobInfo",
                        cause = givenError
                    )
                )
            )
        }
    }

    @Test
    fun `Given database does already exist, when creating external table, then fail`() {

        runTest {

            // given we have a valid mock database info path and creating database doesn't fail
            val givenPath = "mock://path/dir/db.sql"
            val givenJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenPath),
                table = ExternalDatabaseTable(
                    name = "givenTableName",
                    languageFrom = "languageFrom",
                    languageTo = "languageTo"
                ),
                size = 5000
            )

            val givenError = OperationResult.Failure("database already exists")
            databaseRepository.stub {
                on { createDatabase(givenPath) }.doReturn(givenError)
            }

            tableRepository.stub {
                on { createTable(givenJobInfo) }.doReturn(OperationResult.Success)
            }

            val useCase = CreateExternalTable(
                databaseRepository = databaseRepository,
                tableRepository = tableRepository
            )

            val actualExternalTableCreationResult = useCase.invoke(givenJobInfo)

            assertThat(
                actualExternalTableCreationResult,
                equalTo(
                    OperationResult.Failure(
                        errorMessage = "DatabaseRepository::createDatabase failed with argument: $givenPath",
                        cause = givenError
                    )
                )
            )
        }
    }
}
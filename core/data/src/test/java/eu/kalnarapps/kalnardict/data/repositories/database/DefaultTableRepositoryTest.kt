package eu.kalnarapps.kalnardict.data.repositories.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.datasources.words.RandomWordRepository
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationCreation
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationEntry
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class DefaultTableRepositoryTest {

    private val randomWordRepository: RandomWordRepository = mock()
    private val tableManager: ExternalDatabaseGateway.TableManager = mock()
    private val wordManager: ExternalDatabaseGateway.WordManager = mock()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Test
    fun `Given that table doesn't exist, when creating external db, then succeed`() {

        testCoroutineRule.runBlockingTest {

            // given we have a valid database path
            val givenDbPath = "mock://path/dir/db.sql"
            val givenExternalTableCreationJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenDbPath),
                table = ExternalDatabaseTable(
                    "givenName",
                    "givenLanguageFrom",
                    "givenLanguageTo"
                ),
                size = 4000
            )

            // given that table doesn't exist, creating empty table succeeds
            tableManager.stub {
                onBlocking {
                    createEmptyTable(givenDbPath, "givenName", "givenLanguageFrom", "givenLanguageTo")
                }.thenReturn(OperationResult.Success)
            }

            `when`(randomWordRepository.getRandomWord(any())).thenReturn("random")
            `when`(
                wordManager.addWords(
                    argThat {
                        this.dbPath == givenDbPath &&
                                tableName == "givenName" &&
                                externalTranslationEntries.all { it.translation == "random" }
                    }
                )
            ).thenReturn(OperationResult.Success)

            val repository = DefaultTableRepository(
                randomWordRepository = randomWordRepository,
                tableManager = tableManager,
                wordManager = wordManager
            )

            val createTableResult = repository.createTable(givenExternalTableCreationJobInfo)

            assertThat(
                createTableResult,
                equalTo(OperationResult.Success)
            )

            verify(randomWordRepository, times(4000)).getRandomWord(any())
            verify(wordManager, times(10)).addWords(any())
            verify(tableManager).createEmptyTable(
                givenDbPath,
                "givenName",
                "givenLanguageFrom",
                "givenLanguageTo"
            )

        }

    }


    @Test
    fun `Given that table does exist, when creating external db, then fail`() {

        testCoroutineRule.runBlockingTest {

            // given we have a valid database path
            val givenDbPath = "mock://path/dir/db.sql"
            val givenExternalTableCreationJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenDbPath),
                table = ExternalDatabaseTable(
                    "givenName",
                    "givenLanguageFrom",
                    "givenLanguageTo"
                ),
                size = 4000
            )

            // given that table does exist, creating empty table fails
            val tableAlreadyExistsError = OperationResult.Failure("table already exists")
            tableManager.stub {
                onBlocking {
                    createEmptyTable(givenDbPath, "givenName", "givenLanguageFrom", "givenLanguageTo")
                }.thenReturn(tableAlreadyExistsError)
            }

            `when`(randomWordRepository.getRandomWord(any())).thenReturn("random")
            `when`(
                wordManager.addWords(
                    argThat {
                        this.dbPath == givenDbPath &&
                                tableName == "givenName" &&
                                externalTranslationEntries.all { it.translation == "random" }
                    }
                )
            ).thenReturn(OperationResult.Success)

            val repository = DefaultTableRepository(
                randomWordRepository = randomWordRepository,
                tableManager = tableManager,
                wordManager = wordManager
            )

            val createTableResult = repository.createTable(givenExternalTableCreationJobInfo)

            assertThat(
                createTableResult,
                equalTo(
                    OperationResult.Failure(
                        errorMessage = "TableManager::createEmptyTable failed with $givenExternalTableCreationJobInfo",
                        cause = tableAlreadyExistsError
                    )
                )
            )

            verify(tableManager).createEmptyTable(
                givenDbPath,
                "givenName",
                "givenLanguageFrom",
                "givenLanguageTo"
            )

        }
    }

    @Test
    fun `Given that table doesn't exist but word manager can't add words, when creating external db, then fail`() {

        testCoroutineRule.runBlockingTest {

            // given we have a valid database path
            val givenDbPath = "mock://path/dir/db.sql"
            val givenExternalTableCreationJobInfo = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile(givenDbPath),
                table = ExternalDatabaseTable(
                    "givenName",
                    "givenLanguageFrom",
                    "givenLanguageTo"
                ),
                size = 4000
            )

            // given that table doesn't exist, creating empty table succeeds
            tableManager.stub {
                onBlocking {
                    createEmptyTable(givenDbPath, "givenName", "givenLanguageFrom", "givenLanguageTo")
                }.thenReturn(OperationResult.Success)
            }

            `when`(randomWordRepository.getRandomWord(any())).thenReturn("random")
            val addWordError = OperationResult.Failure("word manager not available")
            val givenTranslationCreation = ExternalTranslationCreation(
                dbPath = givenDbPath,
                tableName = "givenName",
                externalTranslationEntries = (0 until 400).map {
                    ExternalTranslationEntry(
                        it, "word #$it", "word #$it", "random"
                    )
                }
            )
            `when`(
                wordManager.addWords(
                    givenTranslationCreation
                )
            ).thenReturn(addWordError)


            val repository = DefaultTableRepository(
                randomWordRepository = randomWordRepository,
                tableManager = tableManager,
                wordManager = wordManager
            )

            val createTableResult = repository.createTable(givenExternalTableCreationJobInfo)

            assertThat(
                createTableResult,
                equalTo(
                    OperationResult.Failure(
                        errorMessage = "WordManager::addWords failed with $givenTranslationCreation",
                        cause = addWordError
                    )
                )
            )

            verify(tableManager).createEmptyTable(
                givenDbPath,
                "givenName",
                "givenLanguageFrom",
                "givenLanguageTo"
            )

        }
    }


}

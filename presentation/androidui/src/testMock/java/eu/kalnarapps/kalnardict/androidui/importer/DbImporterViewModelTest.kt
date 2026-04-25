package eu.kalnarapps.kalnardict.androidui.importer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.database.CreateMockTableUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetMockDatabasesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.presentation.models.mock.DbImporterUi
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData
import eu.kalnarapps.kalnardict.test.TestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.reset
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class DbImporterViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val logger: UiLogger = mock()

    private val mockTableUseCaseFromUi: CreateMockTableUseCaseFromUi = mock()
    private val getAvailableLanguages: ListRegisteredLanguagesUseCaseForUi = mock()
    private val mockGetDatabases: GetMockDatabasesUseCaseForUi = mock()

    private lateinit var viewModel: DbImporterViewModel

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    module {

                    }
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        reset(mockTableUseCaseFromUi)
        reset(getAvailableLanguages)
        reset(mockGetDatabases)
    }

    @Test
    fun `Given there is no language nor databases, when view model initialized, then state is initialized with empty lists`() {

        val unconfinedDispatcher = UnconfinedTestDispatcher()
        runTest(unconfinedDispatcher) {

            getAvailableLanguages.stub {
                on { invoke() }.doReturn(emptyList())
            }
            mockGetDatabases.stub {
                on { invoke() }.doReturn(DataOperationResult.Success(emptyList()))
            }

            viewModel = DbImporterViewModel(
                dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
                logger,
                mockTableUseCaseFromUi,
                getAvailableLanguages,
                mockGetDatabases
            )

            val expectedState = DbImporterUi(
                availableLanguages = emptyList(),
                availableDatabases = emptyList(),
                formData = ImporterFormData(
                    databaseName = "",
                    tableName = "",
                    sourceLanguage = "",
                    destinationLanguage = ""
                ),
                showLoader = false
            )

            unconfinedDispatcher.scheduler.advanceUntilIdle()

            assertThat(
                viewModel.getUiState().value,
                equalTo(expectedState)
            )
        }
    }


    @Test
    fun `Given there are registered languages and databases, when view model initialized, then state is initialized with correct lists`() {

        val unconfinedDispatcher = UnconfinedTestDispatcher()

        val givenLanguages = listOf(
            SelectableLanguage.LanguageUi("language 1", "lang1"),
            SelectableLanguage.LanguageUi("language 2", "lang2")
        )
        val givenDatabases = listOf("database1", "database2")

        getAvailableLanguages.stub {
            on { invoke() }.doReturn(givenLanguages)
        }
        mockGetDatabases.stub {
            on { invoke() }.doReturn(DataOperationResult.Success(givenDatabases))
        }

        viewModel = DbImporterViewModel(
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            logger,
            mockTableUseCaseFromUi,
            getAvailableLanguages,
            mockGetDatabases
        )

        val expectedState = DbImporterUi(
            availableLanguages = givenLanguages,
            availableDatabases = givenDatabases,
            formData = ImporterFormData(
                databaseName = "",
                tableName = "",
                sourceLanguage = "",
                destinationLanguage = ""
            ),
            showLoader = false
        )

        assertThat(
            viewModel.getUiState().value,
            equalTo(expectedState)
        )
    }

    @Test
    fun `Given there are registered languages and databases, when form data validates, then call create mock tables`() {

        val unconfinedDispatcher = UnconfinedTestDispatcher()
        runTest(unconfinedDispatcher) {

            val givenLanguages = listOf(
                SelectableLanguage.LanguageUi("language 1", "lang1"),
                SelectableLanguage.LanguageUi("language 2", "lang2")
            )
            val givenDatabases = listOf("database1", "database2")
            val givenFormData = ImporterFormData(
                databaseName = "name",
                tableName = "table",
                sourceLanguage = "from language",
                destinationLanguage = "to language"
            )
            val givenDbPath = "path/dbpath/db.sql"

            getAvailableLanguages.stub {
                on { invoke() }.doReturn(givenLanguages)
            }
            mockGetDatabases.stub {
                on { invoke() }.doReturn(DataOperationResult.Success(givenDatabases))
            }
            mockTableUseCaseFromUi.stub {
                on { invoke(givenFormData) }.thenReturn(DataOperationResult.Success(givenDbPath))
            }

            viewModel = DbImporterViewModel(
                dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
                logger,
                mockTableUseCaseFromUi,
                getAvailableLanguages,
                mockGetDatabases
            )

            var navigationEvent: String = ""

            val collectJob = launch(unconfinedDispatcher) {
                viewModel.navigationEvent.collect {
                    navigationEvent = it
                }
            }

            viewModel.onFormValidation(givenFormData)

            unconfinedDispatcher.scheduler.advanceUntilIdle()

            verifyBlocking(mockTableUseCaseFromUi) { invoke(givenFormData) }

            assertThat(
                navigationEvent,
                equalTo(givenDbPath)
            )
            collectJob.cancel()
        }
    }
}
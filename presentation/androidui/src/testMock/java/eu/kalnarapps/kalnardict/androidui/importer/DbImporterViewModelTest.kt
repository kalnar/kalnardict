package eu.kalnarapps.kalnardict.androidui.importer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import eu.kalnarapps.kalnardict.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.database.CreateMockTableUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetMockDatabasesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.presentation.models.mock.DbImporterUi
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.test.TestCoroutineRule
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
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.reset
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyBlocking

class DbImporterViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
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

        getAvailableLanguages.stub {
            onBlocking { invoke() }.doReturn(emptyList())
        }
        mockGetDatabases.stub {
            onBlocking { invoke() }.doReturn(DataOperationResult.Success(emptyList()))
        }

        viewModel = DbImporterViewModel(
            dispatcherProvider = TestDispatcherProvider,
            logger,
            mockTableUseCaseFromUi,
            getAvailableLanguages,
            mockGetDatabases
        )

        val testObserver: Observer<DbImporterUi> = mock()
        viewModel.getUiState().observeForever(testObserver)

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

        val uiModels: ArgumentCaptor<DbImporterUi> =
            ArgumentCaptor.forClass(DbImporterUi::class.java)
        verify(testObserver, times(1)).onChanged(
            uiModels.capture()
        )

        viewModel.getUiState().removeObserver(testObserver)
        assertThat(
            uiModels.value,
            equalTo(expectedState)
        )
    }


    @Test
    fun `Given there are registered languages and databases, when view model initialized, then state is initialized with correct lists`() {

        val givenLanguages = listOf(
            SelectableLanguage.LanguageUi("language 1", "lang1"),
            SelectableLanguage.LanguageUi("language 2", "lang2")
        )
        val givenDatabases = listOf("database1", "database2")

        getAvailableLanguages.stub {
            onBlocking { invoke() }.doReturn(givenLanguages)
        }
        mockGetDatabases.stub {
            onBlocking { invoke() }.doReturn(DataOperationResult.Success(givenDatabases))
        }

        viewModel = DbImporterViewModel(
            dispatcherProvider = TestDispatcherProvider,
            logger,
            mockTableUseCaseFromUi,
            getAvailableLanguages,
            mockGetDatabases
        )

        val testObserver: Observer<DbImporterUi> = mock()
        viewModel.getUiState().observeForever(testObserver)

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

        val uiModels: ArgumentCaptor<DbImporterUi> =
            ArgumentCaptor.forClass(DbImporterUi::class.java)
        verify(testObserver).onChanged(
            uiModels.capture()
        )

        viewModel.getUiState().removeObserver(testObserver)
        assertThat(
            uiModels.value,
            equalTo(expectedState)
        )
    }

    @Test
    fun `Given there are registered languages and databases, when form data validates, then call create mock tables`() {

        val givenLanguages = listOf(
            SelectableLanguage.LanguageUi("language 1", "lang1"),
            SelectableLanguage.LanguageUi("language 2", "lang2")
        )
        val givenDatabases = listOf("database1", "database2")
        val givenFormData = ImporterFormData(
            databaseName = "name",
            tableName = "taable",
            sourceLanguage = "from language",
            destinationLanguage = "to language"
        )
        val givenDbPath = "path/dbpath/db.sql"

        getAvailableLanguages.stub {
            onBlocking { invoke() }.doReturn(givenLanguages)
        }
        mockGetDatabases.stub {
            onBlocking { invoke() }.doReturn(DataOperationResult.Success(givenDatabases))
        }
        mockTableUseCaseFromUi.stub {
            onBlocking { invoke(givenFormData) }.thenReturn(DataOperationResult.Success(givenDbPath))
        }

        viewModel = DbImporterViewModel(
            dispatcherProvider = TestDispatcherProvider,
            logger,
            mockTableUseCaseFromUi,
            getAvailableLanguages,
            mockGetDatabases
        )

        viewModel.onFormValidation(givenFormData)

        verifyBlocking(mockTableUseCaseFromUi) { invoke(givenFormData) }

        val testObserver: Observer<DbImporterUi> = mock()
        viewModel.getUiState().observeForever(testObserver)

        val navigationObserver: Observer<NavigationCommand> = mock()
        viewModel.navigationCommand.observeForever(navigationObserver)

        val expectedNavigation = NavigationCommand.Common.NavigateToDictionaryRegistry(givenDbPath)

        val navigationCommand: ArgumentCaptor<NavigationCommand> =
            ArgumentCaptor.forClass(NavigationCommand::class.java)
        verify(navigationObserver).onChanged(
            navigationCommand.capture()
        )

        viewModel.navigationCommand.removeObserver(navigationObserver)
        assertThat(
            navigationCommand.value,
            equalTo(expectedNavigation)
        )
    }


}
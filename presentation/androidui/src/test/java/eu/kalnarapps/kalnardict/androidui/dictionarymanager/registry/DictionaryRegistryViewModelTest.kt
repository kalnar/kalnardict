package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryListMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionaryMockWithFailures
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionarySuccessfullyMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.MockRegisterLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetExternalDbInfoUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.test.TestCoroutineDispatcherProvider
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import  org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub


class DictionaryRegistryViewModelTest : KoinComponent {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val logger = TestLogger()

    private lateinit var getExternalDbInfoMock: GetExternalDbInfoUseCaseForUi
    private lateinit var listAvailableLanguagesMock: ListRegisteredLanguagesUseCaseForUi
    private lateinit var viewModel: DictionaryRegistryViewModel

    private val unconfinedDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    module {
                        single { ReadExternalDbUseCaseMock() as ReadExternalDbUseCase }
                        single {
                            DictionaryListMock().apply {
                                addAll(UiStubs.ManageableDictionaries.manageableDictionaryViews)
                            }
                        }
                    }
                )
            )
        }
        getExternalDbInfoMock = mock()
        listAvailableLanguagesMock = mock()

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun list_found_tables_in_ui() {
        getExternalDbInfoMock.stub {
            on {
                invoke(UiStubs.Uris.validUri)
            }.doReturn(
                DataOperationResult.Success(
                    listOf(UiStubs.TableUiInfo.externalTable1)
                )
            )
        }
        listAvailableLanguagesMock.stub {
            on {
                invoke()
            }.doReturn(
                listOf(UiStubs.TableUiInfo.langUiEn)
            )
        }

        viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getExternalDbInfoMock,
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            listAvailableLanguages = listAvailableLanguagesMock,
            addNewLanguage = MockRegisterLanguageUseCase(),
            uiLogger = logger
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )
    }

    @Test
    fun load_view_model_with_erroneous_uri() {

        getExternalDbInfoMock.stub {
            on {
                invoke(UiStubs.Uris.invalidUri)
            }.doReturn(
                DataOperationResult.Failure(
                    "uri not correct"
                )
            )
        }
        listAvailableLanguagesMock.stub {
            on {
                invoke()
            }.doReturn(
                listOf(UiStubs.TableUiInfo.langUiEn)
            )
        }

        viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.invalidUri,
            loadDbMetaInfoOnDb = getExternalDbInfoMock,
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            listAvailableLanguages = listAvailableLanguagesMock,
            addNewLanguage = MockRegisterLanguageUseCase(),
            uiLogger = logger
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            IsEmptyCollection()
        )
        assertThat<String>(
            viewModel.error.value?.logMessage,
            equalTo(
                "uri not correct\ncause: source cause"
            )
        )
    }


    @Test
    fun update_registering_table_information() {

        getExternalDbInfoMock.stub {
            on {
                invoke(UiStubs.Uris.validUri)
            }.doReturn(
                DataOperationResult.Success(
                    listOf(UiStubs.TableUiInfo.externalTable1)
                )
            )
        }
        listAvailableLanguagesMock.stub {
            on {
                invoke()
            }.doReturn(
                listOf(UiStubs.TableUiInfo.langUiEn)
            )
        }

        viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getExternalDbInfoMock,
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            listAvailableLanguages = listAvailableLanguagesMock,
            addNewLanguage = MockRegisterLanguageUseCase(),
            uiLogger = logger
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )

        val firstTable = listOfTableUi.first()
        val firstTableNewDictionaryName = "mock new name"


        assertThat(
            firstTable.dictionaryName,
            not(equalTo(firstTableNewDictionaryName))
        )

        viewModel.onTableRegisteringUpdate(
            firstTable.copy(dictionaryName = firstTableNewDictionaryName)
        )

        val listOfTableUiModels = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }
        val firstTableFromNewFetch = listOfTableUiModels.first()

        assertThat(
            firstTableFromNewFetch.dictionaryName,
            equalTo(firstTableNewDictionaryName)
        )
    }

    @Test
    fun register_all_dictionaries_with_success() {

        getExternalDbInfoMock.stub {
            on {
                invoke(UiStubs.Uris.validUri)
            }.doReturn(
                DataOperationResult.Success(
                    listOf(UiStubs.TableUiInfo.externalTable1)
                )
            )
        }
        listAvailableLanguagesMock.stub {
            on {
                invoke()
            }.doReturn(
                listOf(UiStubs.TableUiInfo.langUiEn)
            )
        }

        viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getExternalDbInfoMock,
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            listAvailableLanguages = listAvailableLanguagesMock,
            addNewLanguage = MockRegisterLanguageUseCase(),
            uiLogger = logger
        )

        val tables = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            viewModel.error.value,
            IsNull()
        )
        assertThat(
            tables,
            not(IsEmptyCollection())
        )

        viewModel.registerDictionaries()

        assertThat(
            viewModel.error.value,
            IsNull()
        )
    }

    @Test
    fun register_all_dictionaries_with_success_but_last() {

        getExternalDbInfoMock.stub {
            on {
                invoke(UiStubs.Uris.validUri)
            }.doReturn(
                DataOperationResult.Success(
                    listOf(
                        UiStubs.TableUiInfo.externalTable1,
                        UiStubs.TableUiInfo.externalTable2
                    )
                )
            )
        }
        listAvailableLanguagesMock.stub {
            on {
                invoke()
            }.doReturn(
                listOf(UiStubs.TableUiInfo.langUiEn)
            )
        }

        viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getExternalDbInfoMock,
            registerNewDictionary = RegisterNewDictionaryMockWithFailures(listOf(2)),
            dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
            listAvailableLanguages = listAvailableLanguagesMock,
            addNewLanguage = MockRegisterLanguageUseCase(),
            uiLogger = logger
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable1
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable2
        )

        val tables = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            viewModel.error.value,
            IsNull()
        )
        assertThat(
            tables.filter { it.isSelected },
            IsCollectionWithSize(equalTo(2))
        )

        viewModel.registerDictionaries()

        assertThat(
            viewModel.error.value,
            not(IsNull())
        )

        val lastResult = viewModel.getRegistrationStatus().last().progress
        assertThat(
            lastResult,
            IsInstanceOf(DataOperationResult.Failure::class.java)
        )

        check(lastResult is DataOperationResult.Failure)

        assertThat(
            lastResult.errorMessage(),
            containsString(UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG)
        )
    }
}
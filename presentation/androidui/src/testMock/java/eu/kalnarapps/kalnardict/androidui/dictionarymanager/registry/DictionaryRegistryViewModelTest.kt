package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionaryMockWithFailures
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionarySuccessfullyMock
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class DictionaryRegistryViewModelTest : KoinComponent {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    module {
                        single { ReadExternalDbUseCaseMock() as ReadExternalDbUseCase }
                    }
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun list_found_tables_in_ui() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider
        )

        val listOfTableUi = viewModel.getTables()

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )
    }

    @Test
    fun load_view_model_with_erroneous_uri() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.invalidUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider
        )

        val listOfTableUi = viewModel.getTables()

        assertThat(
            listOfTableUi,
            IsEmptyCollection()
        )
        assertThat<String>(
            viewModel.error.value?.logMessage,
            containsString("not of correct")
        )
    }


    @Test
    fun update_registering_table_information() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider
        )

        val listOfTableUi = viewModel.getTables()

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

        val firstTableFromNewFetch = viewModel.getUiState().value?.tableInfoUiModels?.first()

        assertThat(
            firstTableFromNewFetch?.dictionaryName,
            equalTo(firstTableNewDictionaryName)
        )

    }

    @Test
    fun register_all_dictionaries_with_success() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider
        )

        val tables = viewModel.getTables()

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

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionaryMockWithFailures(listOf(2)),
            dispatcherProvider = TestDispatcherProvider
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable1
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable2
        )

        val tables = viewModel.getUiState().value?.tableInfoUiModels

        assertThat(
            viewModel.error.value,
            IsNull()
        )
        assertThat(
            tables?.filter { it.isSelected },
            IsCollectionWithSize(equalTo(2))
        )

        viewModel.registerDictionaries()

        assertThat(
            viewModel.error.value,
            not(IsNull())
        )

        val lastResult = viewModel.getRegistrationStatus().last().result
        assertThat(
            lastResult,
            IsInstanceOf(OperationResult.Failure::class.java)
        )

        check(lastResult is OperationResult.Failure)

        assertThat(
            lastResult,
            equalTo(OperationResult.Failure(UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG))
        )
    }
}
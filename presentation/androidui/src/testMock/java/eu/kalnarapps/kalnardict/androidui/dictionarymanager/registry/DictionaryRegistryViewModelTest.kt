package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionaryMockWithFailures
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionarySuccessfullyMock
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsEmptyCollection
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
            RegisterNewDictionarySuccessfullyMock()
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
            RegisterNewDictionarySuccessfullyMock()
        )

        val listOfTableUi = viewModel.getTables()
        val errorMsg = viewModel.getErrors()
        errorMsg.observeForever {}

        assertThat(
            listOfTableUi,
            IsEmptyCollection()
        )
        assertThat<String>(
            errorMsg.value?.first(),
            containsString("not of correct")
        )
    }


    @Test
    fun update_registering_table_information() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock()
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

        val firstTableFromNewFetch = viewModel.getTables().first()

        assertThat(
            firstTableFromNewFetch.dictionaryName,
            equalTo(firstTableNewDictionaryName)
        )

    }

    @Test
    fun register_all_dictionaries_with_success() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionarySuccessfullyMock()
        )

        val errors = viewModel.getErrors()
        errors.observeForever { }
        val tables = viewModel.getTables()

        assertThat(
            errors.value,
            IsEmptyCollection()
        )
        assertThat(
            tables,
            not(IsEmptyCollection())
        )

        viewModel.registerDictionaries()

        assertThat(
            errors.value,
            IsEmptyCollection()
        )
    }

    @Test
    fun register_all_dictionaries_with_success_but_last() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get(),
            RegisterNewDictionaryMockWithFailures(listOf(2))
        )

        val errors = viewModel.getErrors()
        errors.observeForever { }
        val tables = viewModel.getTables()

        assertThat(
            errors.value,
            IsEmptyCollection()
        )
        assertThat(
            tables,
            not(IsEmptyCollection())
        )

        viewModel.registerDictionaries()

        assertThat(
            errors.value,
            not(IsEmptyCollection())
        )
    }
}
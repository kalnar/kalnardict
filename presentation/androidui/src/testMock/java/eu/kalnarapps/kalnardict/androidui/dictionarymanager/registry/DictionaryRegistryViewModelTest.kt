package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsEmptyCollection
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
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

    @Test
    fun list_found_tables_in_ui() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri.path,
            getKoin().get()
        )

        val listOfTableUi = viewModel.getTables()

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )
    }

    @Test
    fun load_view_model_with_errored_uri() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.invalidUri.path,
            getKoin().get()
        )

        val listOfTableUi = viewModel.getTables()
        val errorMsg = viewModel.getErrors()
        errorMsg.observeForever {}

        assertThat(
            listOfTableUi,
            IsEmptyCollection()
        )
        assertThat<String>(
            errorMsg.value,
            containsString("not of correct")
        )
    }


}
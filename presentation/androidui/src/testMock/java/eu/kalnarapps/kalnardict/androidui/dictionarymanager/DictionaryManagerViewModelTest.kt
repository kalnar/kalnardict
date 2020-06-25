package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.dependencies.mockRepositoryModule
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.core.IsIterableContaining
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class DictionaryManagerViewModelTest : KoinComponent {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    mockRepositoryModule,
                    module {
                        single {
                            DictionaryListMock().apply {
                                addAll(Stubs.Domain.Dictionaries.ALL)
                            }
                        }
                        viewModel {
                            DictionaryManagerViewModel(
                                listRegisteredDictionariesUseCase = ListDictionariesMock(
                                    get()
                                )
                            )
                        }
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
    fun list_dictionaries() {
        testCoroutineRule.runBlockingTest {

            val viewModel: DictionaryManagerViewModel = getKoin().get()
            val dicts = viewModel.getRegisteredDictionaries()

            assertThat(
                dicts,
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "dictionaryName",
                        containsString("english")
                    )
                )
            )
            assertThat(
                dicts, not(
                    IsIterableContaining(
                        HasPropertyWithValue<String>(
                            "dictionaryName",
                            containsString("russian")
                        )
                    )
                )
            )
        }
    }

//    @Test
//    fun add_external_dictionary_table() {
//        testCoroutineRule.runBlockingTest {
//
//            val viewModel: DictionaryManagerViewModel = getKoin().get()
//
//            // when
//            viewModel.registerNewDictionary(UiStubs.DictionaryManager.externalTable)
//
//            // then
//            val dicts = viewModel.getRegisteredDictionaries()
//            assertThat(
//                dicts,
//                IsIterableContaining(
//                    HasPropertyWithValue<String>(
//                        "dictionaryName",
//                        containsString(UiStubs.DictionaryManager.newDictionaryName)
//                    )
//                )
//            )
//        }
//
//    }
}
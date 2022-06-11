package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.dependencies.mockRepositoryModule
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.MockUpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.test.TestCoroutineRule
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.HasPropertyWithValue
import org.hamcrest.core.IsInstanceOf
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
    private val logger = TestLogger()


    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    mockRepositoryModule,
                    module {
                        single {
                            DictionaryListMock().apply {
                                addAll(UiStubs.ManageableDictionaries.manageableDictionaryViews)
                            }
                        }
                        viewModel {
                            DictionaryManagerViewModel(
                                listRegisteredDictionariesUseCase = ListDictionariesMock(
                                    get()
                                ),
                                uiLogger = logger,
                                updateRenderingStrategy = MockUpdateDictionaryUseCaseFromUi(),
                                dispatcherProvider = TestDispatcherProvider
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
            val dictsLiveData = viewModel.getRegisteredDictionaries()
            dictsLiveData.observeForever {}

            val content = dictsLiveData.value
            assertThat(
                content,
                IsInstanceOf(LoadableContent.Completed::class.java)
            )
            assertThat(
                (content as LoadableContent.Completed).content,
                IsIterableContaining(
                    HasPropertyWithValue<String>(
                        "dictionaryName",
                        equalTo(UiStubs.ManageableDictionaries.englishDictName)
                    )
                )
            )

        }
    }

}
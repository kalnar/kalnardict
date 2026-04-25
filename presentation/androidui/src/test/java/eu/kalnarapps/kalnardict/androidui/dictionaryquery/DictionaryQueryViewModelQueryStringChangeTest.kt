package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.Observer
import eu.kalnarapps.kalnardict.androidui.stubs.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ChangeDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesFlowUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.UpdateQueryModeUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.words.GetTranslationUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import eu.kalnarapps.kalnardict.test.TestCoroutineDispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableWithSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub


class DictionaryQueryViewModelQueryStringChangeTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var listRegisteredDictionariesUseCase: ListRegisteredDictionariesFlowUseCaseForUi

    @Mock
    lateinit var searchQueryUseCase: SearchQueryUseCaseFromUi

    @Mock
    lateinit var changeDictLanguageUseCase: ChangeDictionaryUseCaseFromUi

    @Mock
    lateinit var getLanguageUseCase: GetCurrentDictionaryUseCaseForUi

    @Mock
    lateinit var getTranslationUseCase: GetTranslationUseCaseForUi

    @Mock
    lateinit var getQueryModesForUi: GetQueryModesUseCaseForUi

    @Mock
    lateinit var updateQueryModeUseCase: UpdateQueryModeUseCaseFromUi

    private lateinit var viewModel: DictionaryQueryViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this).close()
    }

    @Test
    fun when_query_string_changed_refresh_word_list() {
        val unconfinedDispatcher = UnconfinedTestDispatcher()
        runTest(unconfinedDispatcher) {

            val languageFlow = MutableStateFlow<DictionarySelection>(
                DictionarySelection.Current(UiStubs.Ui.Dictionaries.englishToEnglish)
            )
            `when`(getLanguageUseCase.invoke()).thenReturn(languageFlow)
            `when`(searchQueryUseCase.invoke(any())).thenReturn(
                UiStubs.Ui.Words.words
            )
            searchQueryUseCase.stub {
                on {
                    invoke(
                        QueryUiModel(
                            "1",
                            UiStubs.Ui.Dictionaries.englishToEnglish,
                            UiStubs.Ui.QueryMode.anywhere.copy(isSelected = true)
                        )
                    )
                }.doReturn(
                    UiStubs.Ui.Words.words.filter { it.baseForm.contains("1") }
                )
            }
            `when`(getQueryModesForUi()).thenReturn(
                flowOf(
                    listOf(
                        UiStubs.Ui.QueryMode.anywhere.copy(isSelected = true),
                        UiStubs.Ui.QueryMode.beginning
                    )
                )
            )
            val dictionariesFlow =
                MutableStateFlow(listOf(UiStubs.Ui.Dictionaries.englishToEnglish))
            `when`(listRegisteredDictionariesUseCase.invoke()).thenReturn(dictionariesFlow)

            viewModel = DictionaryQueryViewModel(
                listRegisteredDictionariesUseCase = listRegisteredDictionariesUseCase,
                listQueryResultsUseCase = searchQueryUseCase,
                updateCurrentLanguageUseCase = changeDictLanguageUseCase,
                getCurrentDictionary = getLanguageUseCase,
                dispatcherProvider = TestCoroutineDispatcherProvider(unconfinedDispatcher),
                getTranslation = getTranslationUseCase,
                updateQueryModeUseCase = updateQueryModeUseCase,
                getQueryModesForUi = getQueryModesForUi,
                uiLogger = TestLogger()
            )

            val words = mutableListOf<WordView>()
            val queryResult = viewModel.getQueryResult()
            val testObserver = Observer<QueryResult> {
                words.clear()
                val loadableContent = it.wordList
                if (loadableContent is LoadableContent.Completed) {
                    words.addAll(loadableContent.content)
                }
            }
            viewModel.onQueryChanged(TextFieldValue(""))
            unconfinedDispatcher.scheduler.advanceUntilIdle()
            queryResult.observeForever(testObserver)
            try {
                assertThat(
                    words,
                    IsIterableWithSize(
                        equalTo(
                            UiStubs.Ui.Words.words.size
                        )
                    )
                )


                viewModel.onQueryChanged(TextFieldValue("1"))
                unconfinedDispatcher.scheduler.advanceUntilIdle()

                assertThat(
                    words,
                    IsIterableWithSize(
                        equalTo(
                            UiStubs.Ui.Words.words.filter { it.baseForm.contains("1") }.size
                        )
                    )
                )

            } finally {
                queryResult.removeObserver(testObserver)
            }
        }
    }
}

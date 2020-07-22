package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryResult
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.stubs.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase
import eu.kalnarapps.kalnardict.interactors.GetQueryModesUseCaseForUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableWithSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DictionaryQueryViewModelQueryStringChangeTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase

    @Mock
    lateinit var searchQueryUseCase: SearchQueryUseCase

    @Mock
    lateinit var changeDictLanguageUseCase: ChangeDictLanguageUseCase

    @Mock
    lateinit var getLanguageUseCase: GetLanguageUseCase

    @Mock
    lateinit var getTranslationUseCase: GetTranslationUseCase

    @Mock
    lateinit var getQueryModesForUi: GetQueryModesUseCaseForUi

    @Mock
    lateinit var updateQueryModeUseCase: UpdateQueryModeUseCase

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
        testCoroutineRule.runBlockingTest {

            `when`(getLanguageUseCase.invoke()).thenReturn(
                CurrentDictionary.SetDictionary(UiStubs.Domain.Dictionaries.englishToEnglish)
            )
            `when`(searchQueryUseCase.invokeWith("", 0)).thenReturn(
                UiStubs.Domain.Words.words
            )
            `when`(getQueryModesForUi()).thenReturn(
                flowOf(
                    listOf(
                        UiStubs.Ui.QueryMode.anywhere.copy(isSelected = true),
                        UiStubs.Ui.QueryMode.beginning
                    )
                )
            )
            `when`(listRegisteredDictionariesUseCase.invoke()).thenReturn(
                listOf(UiStubs.Domain.Dictionaries.englishToEnglish)
            )
            doAnswer { invocationOnMock ->
                when (invocationOnMock.arguments[1]) {
                    UiStubs.Ui.QueryMode.beginning.id -> {
                        UiStubs.Domain.Words.words.filter {
                            it.baseForm.startsWith(invocationOnMock.arguments[0] as String)
                        }
                    }
                    UiStubs.Ui.QueryMode.anywhere.id -> {
                        UiStubs.Domain.Words.words.filter {
                            it.baseForm.contains(invocationOnMock.arguments[0] as String)
                        }
                    }
                    else -> {
                        emptyList()
                    }
                }
            }.`when`(searchQueryUseCase).invokeWith(Mockito.anyString(), Mockito.anyInt())

            viewModel = DictionaryQueryViewModel(
                listRegisteredDictionariesUseCase = listRegisteredDictionariesUseCase,
                listQueryResultsUseCase = searchQueryUseCase,
                updateCurrentLanguageUseCase = changeDictLanguageUseCase,
                getCurrentLanguageUseCase = getLanguageUseCase,
                dispatcherProvider = TestDispatcherProvider,
                getTranslation = getTranslationUseCase,
                updateQueryModeUseCase = updateQueryModeUseCase,
                getQueryModesForUi = getQueryModesForUi,
                uiLogger = TestLogger()
            )


            val words = mutableListOf<WordView>()
            val queryResult = viewModel.getQueryResult()
            val testObserver = Observer<QueryResult> {
                words.clear()
                words.addAll(it.wordList)
            }
            viewModel.onQueryChanged("")
            queryResult.observeForever(testObserver)
            try {
                assertThat(
                    words,
                    IsIterableWithSize(
                        equalTo(
                            UiStubs.Domain.Words.words.size
                        )
                    )
                )


                viewModel.onQueryChanged("1")

                assertThat(
                    words,
                    IsIterableWithSize(
                        equalTo(
                            UiStubs.Domain.Words.words.filter { it.baseForm.contains("1") }.size
                        )
                    )
                )

            } finally {
                queryResult.removeObserver(testObserver)
            }

        }
    }
}

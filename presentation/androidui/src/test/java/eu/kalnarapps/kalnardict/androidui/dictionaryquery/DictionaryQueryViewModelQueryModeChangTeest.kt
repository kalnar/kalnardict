package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.stubs.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ChangeDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.UpdateQueryModeUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.words.GetTranslationUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class DictionaryQueryViewModelQueryModeChangTeest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCaseForUi

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
    fun when_query_mode_changed_refresh_word_list() {
        testCoroutineRule.runBlockingTest {

            val languageFlow = MutableStateFlow<DictionarySelection>(
                DictionarySelection.Current(UiStubs.Ui.Dictionaries.englishToEnglish)
            )
            `when`(getLanguageUseCase.invoke()).thenReturn(languageFlow)
            `when`(searchQueryUseCase.invoke(any())).thenReturn(
                UiStubs.Ui.Words.words
            )
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
                dispatcherProvider = TestDispatcherProvider,
                getTranslation = getTranslationUseCase,
                updateQueryModeUseCase = updateQueryModeUseCase,
                getQueryModesForUi = getQueryModesForUi,
                uiLogger = TestLogger()
            )


            // TODO use mockito to verify onChanged observer instead of using real observers
            val words = mutableListOf<WordView>()
            val queryResult = viewModel.getQueryResult()
            val testObserver = Observer<QueryResult> {
                words.clear()
                val loadableContent = it.wordList
                if (loadableContent is LoadableContent.Completed) {
                    words.addAll(loadableContent.content)
                }
            }
            queryResult.observeForever(testObserver)
            viewModel.onQueryChanged("2")
            try {
                assertThat(
                    words,
                    IsIterableWithSize(
                        equalTo(
                            UiStubs.Ui.Words.words.filter { it.baseForm.contains("2") }.size
                        )
                    )
                )


                viewModel.onQueryModeChanged(
                    QueryModelUiModel(
                        id = UiStubs.Ui.QueryMode.anywhere.id,
                        displayString = "hello"
                    )
                )

                verify(updateQueryModeUseCase).invoke(UiStubs.Ui.QueryMode.anywhere.id)

            } finally {
                queryResult.removeObserver(testObserver)
                viewModel.viewModelScope.cancel()
            }

        }
    }
}

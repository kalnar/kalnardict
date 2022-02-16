package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryListMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.ListDictionariesMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockGetDictionaryUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockGetLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockGetTranslationUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockSearchQueryUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.model.MockDictEntry
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toDictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toWordView
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.androidui.test.assertThat
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase
import eu.kalnarapps.kalnardict.interactors.GetQueryModesUseCaseForUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*


class DictionaryQueryViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val logger = TestLogger()

    private val getQueryModesUseCaseForUi = mock(GetQueryModesUseCaseForUi::class.java)

    @Test
    fun when_a_word_is_selected_navigate_to_translation_and_load_translation() {
        testCoroutineRule.runBlockingTest {

            setUp()

            val wordToTranslate = UiUnitTestStubs.Words.wordTake
            val translation = UiUnitTestStubs.Words.wordTakeTranslationInFrench
            val translationDictionary = UiUnitTestStubs.Dictionaries.englishToFrenchDictionary

            val viewModel = DictionaryQueryViewModel(
                listQueryResultsUseCase = MockSearchQueryUseCase(),
                listRegisteredDictionariesUseCase = ListDictionariesMock(DictionaryListMock()),
                updateCurrentLanguageUseCase = MockChangeDictLanguageUseCase(),
                getCurrentLanguageUseCase = MockGetLanguageUseCase(
                    CurrentDictionary.SetDictionary(translationDictionary)
                ),
                getTranslation = MockGetTranslationUseCase(
                    mockEntries = listOf(
                        MockDictEntry(
                            word = wordToTranslate,
                            translation = translation,
                            dictionary = translationDictionary
                        )
                    ),
                    currentDictionary = CurrentDictionary.SetDictionary(translationDictionary)
                ),
                dispatcherProvider = TestDispatcherProvider,
                getQueryModesForUi = getQueryModesUseCaseForUi,
                updateQueryModeUseCase = mock(UpdateQueryModeUseCase::class.java),
                uiLogger = logger
            )
            val translationObserver = viewModel.getTranslation().test()
                .awaitValue()
                .assertThat(
                    equalTo<LoadableContent<*>>(LoadableContent.UnInitialized)
                )

            // when word selected
            viewModel.onWordSelected(wordToTranslate.toWordView())

            // then navigation is posted
            assertThat(
                viewModel.navigationCommand.value,
                equalTo<NavigationCommand>(NavigationCommand.NavigateToDictionaryTranslation)
            )

            val loadTranslation = translationObserver
                .awaitValue()
                .value()

            assertThat(
                loadTranslation,
                IsInstanceOf(LoadableContent.Completed::class.java)
            )
            check(loadTranslation is LoadableContent.Completed)
            assertThat(
                loadTranslation.content,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(loadTranslation.content is DataOperationResult.Success)
            assertThat(
                (loadTranslation.content as DataOperationResult.Success<String>).data,
                equalTo(translation.translation)
            )
        }
    }

    @Test
    fun when_dictionary_changed_load_new_query_result() {
        testCoroutineRule.runBlockingTest {

            setUp()

            val currentDictionaryWrapper = DictionaryWrapper(
                CurrentDictionary.SetDictionary(UiUnitTestStubs.Dictionaries.englishToFrenchDictionary)
            )
            val dictionaries = listOf(
                UiUnitTestStubs.Dictionaries.englishToFrenchDictionary,
                UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary
            )

            val mockEntries = listOf(
                MockDictEntry(
                    word = UiUnitTestStubs.Words.wordPrendre,
                    translation = UiUnitTestStubs.Words.wordPrendreTranslationInFrench,
                    dictionary = UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary
                ),
                MockDictEntry(
                    word = UiUnitTestStubs.Words.wordTake,
                    translation = UiUnitTestStubs.Words.wordTakeTranslationInFrench,
                    dictionary = UiUnitTestStubs.Dictionaries.englishToFrenchDictionary
                )
            )

            val viewModel = DictionaryQueryViewModel(
                listQueryResultsUseCase = MockSearchQueryUseCase(
                    mockEntries,
                    currentDictionaryWrapper
                ),
                listRegisteredDictionariesUseCase = ListDictionariesMock(DictionaryListMock()),
                updateCurrentLanguageUseCase = MockChangeDictLanguageUseCase(
                    currentDictionary = currentDictionaryWrapper,
                    listOfDictionaries = dictionaries
                ),
                getCurrentLanguageUseCase = MockGetDictionaryUseCase(
                    currentDictionaryWrapper
                ),
                getTranslation = MockGetTranslationUseCase(
                    mockEntries = mockEntries,
                    currentDictionary = CurrentDictionary.SetDictionary(
                        UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary
                    )
                ),
                dispatcherProvider = TestDispatcherProvider,
                getQueryModesForUi = getQueryModesUseCaseForUi,
                updateQueryModeUseCase = mock(UpdateQueryModeUseCase::class.java),
                uiLogger = logger
            )
            // TODO: need to remove observer in a finally clause
            viewModel.getQueryResult().test()
                .awaitValue()
                .value()
                .run {
                    assertThat(
                        this.dictionary,
                        equalTo(
                            UiUnitTestStubs.Dictionaries.englishToFrenchDictionary.toDictionarySelectorItem()
                        )
                    )
                }
            val firstWord = viewModel.getQueryResult().test()
                .awaitValue()
                .value()
                .wordList.first()

            // when dictionary changed
            viewModel.onDictionaryChanged(
                UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary.toDictionarySelectorItem()
            )

            // then query results are refreshed
            val updatedResult = viewModel.getQueryResult().test()
                .awaitValue()
                .value()

            val uiState = viewModel.getUiState().value

            assertThat(
                uiState?.currentDictionaryItemView,
                equalTo(
                    UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary.toDictionarySelectorItem()
                )
            )

            assertThat(
                currentDictionaryWrapper.currentDictionary.dictionary,
                equalTo(
                    UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary
                )
            )
            assertThat(
                updatedResult.dictionary,
                equalTo(
                    UiUnitTestStubs.Dictionaries.frenchToFrenchDictionary.toDictionarySelectorItem()
                )
            )
            // TODO this test is all wrong, we should mock daos and do integration tests or just
            // mock the dependencies with mockito and verify that the dependencies are called with
            // correct arguments
            // TODO big refacto of tests after rendering feature to be done
        }
    }

    private suspend fun setUp() {
        `when`(getQueryModesUseCaseForUi()).thenReturn(
            flowOf(
                listOf(
                    UiUnitTestStubs.Ui.QueryMode.anywhere,
                    UiUnitTestStubs.Ui.QueryMode.beginning.copy(isSelected = true)
                )
            )
        )
    }
}


data class DictionaryWrapper(
    var currentDictionary: CurrentDictionary.SetDictionary
)
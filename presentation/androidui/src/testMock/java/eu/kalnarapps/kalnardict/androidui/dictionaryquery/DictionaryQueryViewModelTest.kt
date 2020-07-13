package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryListMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.ListDictionariesMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockGetLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockGetTranslationUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.MockSearchQueryUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.model.MockDictEntry
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toWordView
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.androidui.test.TestLogger
import eu.kalnarapps.kalnardict.androidui.test.assertThat
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class DictionaryQueryViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val logger = TestLogger()

    @Test
    fun when_a_word_is_selected_navigate_to_translation_and_load_translation() {
        testCoroutineRule.runBlockingTest {

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
}


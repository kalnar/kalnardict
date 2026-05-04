package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import eu.kalnarapps.kalnardict.androidui.common.compose.SemanticTags
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModeUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DictionaryQueryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_query_results_then_show_query_list() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(TextFieldValue("words"))
        every { mockViewModel.getQueryModes() } returns MutableLiveData(emptyList())

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {},
                navigateToDictionaryManager = {}
            )
        }

        composeTestRule
            .onNodeWithText("first")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("second")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("third")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("words")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("French English (fr-en) (French English Dictionary)")
            .assertIsDisplayed()
    }

    @Test
    fun when_clicking_on_dictionary_then_list_dictionaries() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    ),
                    DictionaryUiModel(
                        id = 2,
                        displayString = "English French (en-fr)",
                        description = "English French Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(TextFieldValue("words"))
        every { mockViewModel.getQueryModes() } returns MutableLiveData(emptyList())
        every { mockViewModel.onDictionaryChanged(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {},
                navigateToDictionaryManager = {}
            )
        }

        composeTestRule
            .onNodeWithText("French English (fr-en) (French English Dictionary)")
            .performClick()
        composeTestRule
            .onNode(
                hasText(
                    "French English (fr-en) (French English Dictionary)"
                ) and hasTestTag(
                    SemanticTags.dictionarySelectorDropDown
                )
            )
            .assertIsDisplayed()
        composeTestRule
            .onNode(
                hasText(
                    "English French (en-fr) (English French Dictionary)"
                ) and hasTestTag(
                    SemanticTags.dictionarySelectorDropDown
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNode(
                hasText(
                    "English French (en-fr) (English French Dictionary)"
                ) and hasTestTag(
                    SemanticTags.dictionarySelectorDropDown
                )
            )
            .performClick()
        verify {
            mockViewModel.onDictionaryChanged(
                DictionaryUiModel(
                    id = 2,
                    displayString = "English French (en-fr)",
                    description = "English French Dictionary",
                    renderingStrategy = RenderingStrategy("html", "html")
                )
            )
        }
    }

    @Test
    fun when_clicking_on_query_modes_then_list_query_modes() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    ),
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(TextFieldValue("words"))
        every { mockViewModel.getQueryModes() } returns MutableLiveData(
            listOf(
                QueryModeUiModel(
                    id = 1,
                    displayString = "exact",
                    isSelected = false,
                ),
                QueryModeUiModel(
                    id = 2,
                    displayString = "fuzzy",
                    isSelected = false,
                ),
                QueryModeUiModel(
                    id = 3,
                    displayString = "start",
                    isSelected = true,
                ),
            )
        )
        every { mockViewModel.onDictionaryChanged(any()) } returns Unit
        every { mockViewModel.onQueryModeChanged(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {},
                navigateToDictionaryManager = {}
            )
        }

        composeTestRule
            .onNodeWithTag(SemanticTags.queryModeButton)
            .performClick()
        composeTestRule
            .onNodeWithText("exact")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("fuzzy")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("start")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("fuzzy")
            .performClick()

        verify {
            mockViewModel.onQueryModeChanged(
                QueryModeUiModel(
                    id = 2,
                    displayString = "fuzzy",
                    isSelected = false,
                )
            )
        }
    }

    @Test
    fun when_clicking_on_options_then_show_dictionary_manager_and_navigate() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    ),
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(TextFieldValue("words"))
        every { mockViewModel.getQueryModes() } returns MutableLiveData(emptyList())
        every { mockViewModel.onDictionaryChanged(any()) } returns Unit
        every { mockViewModel.onQueryModeChanged(any()) } returns Unit

        var hasNavigatedToDictionaryManager = false

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {},
                navigateToDictionaryManager = {
                    hasNavigatedToDictionaryManager = true
                }
            )
        }

        composeTestRule
            .onNodeWithTag(SemanticTags.queryMoreOptions)
            .performClick()
        composeTestRule
            .onNodeWithText("Dictionary Manager")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Dictionary Manager")
            .performClick()

        assert(hasNavigatedToDictionaryManager)
    }

    @Test
    fun when_clicking_on_result_item_navigate_to_translation_view() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    ),
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(TextFieldValue("words"))
        every { mockViewModel.getQueryModes() } returns MutableLiveData(emptyList())
        every { mockViewModel.onWordSelected(any()) } returns Unit

        var hasNavigatedToTranslation = false

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {
                    hasNavigatedToTranslation = true
                },
                navigateToDictionaryManager = {}
            )
        }

        composeTestRule
            .onNodeWithText("first")
            .performClick()

        assert(hasNavigatedToTranslation)

        verify {
            mockViewModel.onWordSelected(
                WordView(0, "first"),
            )
        }
    }

    @Test
    fun when_typing_query_then_view_model_on_query_changed_called() {

        val mockViewModel = mockk<DictionaryQueryViewModel>()
        every { mockViewModel.getQueryResult() } returns MutableLiveData(
            QueryResult(
                LoadableContent.Completed(
                    listOf(
                        WordView(0, "first"),
                        WordView(1, "second"),
                        WordView(2, "third")
                    )
                ),
                dictionary = LoadableContent.Completed(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    )
                )
            )
        )
        every {
            mockViewModel.getRegisteredDictionaries()
        } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    DictionaryUiModel(
                        id = 1,
                        displayString = "French English (fr-en)",
                        description = "French English Dictionary",
                        renderingStrategy = RenderingStrategy("html", "html")
                    ),
                )
            )
        )
        every { mockViewModel.typedQuery } returns MutableStateFlow(
            TextFieldValue(
                "word",
                TextRange(4, 4)
            )
        )
        every { mockViewModel.getQueryModes() } returns MutableLiveData(emptyList())
        every { mockViewModel.onQueryChanged(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryQueryScreen(
                viewModel = mockViewModel,
                navigateToTranslation = {},
                navigateToDictionaryManager = {}
            )
        }

        composeTestRule
            .onNodeWithText("word")
            .performTextInput("s")

        verify {
            mockViewModel.onQueryChanged(
                TextFieldValue(
                    "words",
                    TextRange(5, 5)
                )
            )
        }
    }
}
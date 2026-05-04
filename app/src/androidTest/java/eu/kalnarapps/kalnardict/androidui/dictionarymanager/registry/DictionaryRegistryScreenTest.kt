package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.MutableLiveData
import eu.kalnarapps.kalnardict.androidui.common.compose.SemanticTags
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.DictionaryRegistryState
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableStatus
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegisterDictionaryUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegistryError
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class DictionaryRegistryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_register_model_failed_then_show_error_screen() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Failed(OperationResult.Failure("error"))
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Invalid database file selected. No dictionaries found.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("An error has occurred. Go back to dictionary manager.")
            .assertIsDisplayed()
    }

    @Test
    fun when_error_screen_and_click_on_OK_then_navigate_back() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Failed(OperationResult.Failure("error"))
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        var isNavigatedBack = false

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {
                    isNavigatedBack = true
                },
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("OK")
            .performClick()

        assertEquals(true, isNavigatedBack)
    }

    @Test
    fun when_register_model_loads_then_show_form() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 0,
                                dictionaryNameCursorIndexEnd = 0,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = false
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("original table name")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("table_register_switch")
            .assert(isToggleable())
            .assert(isOff())
        composeTestRule
            .onNodeWithText("Name to register:")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("dictionary name")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Source language:")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("original fr")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("original en")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Destination language:")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("French (fr)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("English (en)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Add new language")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Register tables")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Register tables")
            .assertIsNotEnabled()
            .assertIsDisplayed()
    }

    @Test
    fun when_register_model_selected_then_register_button_enabled() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 0,
                                dictionaryNameCursorIndexEnd = 0,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = true
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Register tables")
            .assertIsEnabled()
            .assertIsDisplayed()
    }

    @Test
    fun when_selecting_languages_then_list_available_languages() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 19,
                                dictionaryNameCursorIndexEnd = 19,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = true
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                                SelectableLanguage.LanguageUi("German", "de"),
                                SelectableLanguage.LanguageUi("Spanish", "es"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.onTableRegisteringUpdate(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("French (fr)")
            .performClick()
        composeTestRule
            .onAllNodesWithText("French (fr)")
            .assertCountEquals(2)
        composeTestRule
            .onAllNodesWithText("English (en)")
            .assertCountEquals(2)
        composeTestRule
            .onNodeWithText("German (de)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Spanish (es)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("German (de)")
            .performClick()
        verify {
            mockViewModel.onTableRegisteringUpdate(
                ExternalTableUiInfo(
                    originalTableName = "original table name",
                    dictionaryName = "dictionary name",
                    dictionaryNameCursorIndexStart = 19,
                    dictionaryNameCursorIndexEnd = 19,
                    originalLanguageFrom = "original fr",
                    languageFromUi = SelectableLanguage.LanguageUi("German", "de"),
                    originalLanguageTo = "original en",
                    languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                    isSelected = true
                ),
            )
        }
        composeTestRule
            .onNodeWithText("English (en)")
            .performClick()
        composeTestRule
            .onAllNodesWithText("French (fr)")
            .assertCountEquals(2)
        composeTestRule
            .onAllNodesWithText("English (en)")
            .assertCountEquals(2)
        composeTestRule
            .onNodeWithText("German (de)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Spanish (es)")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Spanish (es)")
            .performClick()
        verify {
            mockViewModel.onTableRegisteringUpdate(
                ExternalTableUiInfo(
                    originalTableName = "original table name",
                    dictionaryName = "dictionary name",
                    dictionaryNameCursorIndexStart = 19,
                    dictionaryNameCursorIndexEnd = 19,
                    originalLanguageFrom = "original fr",
                    languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                    originalLanguageTo = "original en",
                    languageToUi = SelectableLanguage.LanguageUi("Spanish", "es"),
                    isSelected = true
                ),
            )
        }
    }

    @Test
    fun when_click_on_add_new_language_then_show_dialog_and_register_when_validated() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 0,
                                dictionaryNameCursorIndexEnd = 0,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = true
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.onNewLanguageRegistryClicked(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Add new language")
            .performClick()
        composeTestRule
            .onNodeWithTag(SemanticTags.languageDialogCode)
            .performTextInput("ln")
        composeTestRule
            .onNodeWithTag(SemanticTags.languageDialogName)
            .performTextInput("language")
        composeTestRule
            .onNodeWithText("Register language")
            .performClick()
        verify {
            mockViewModel.onNewLanguageRegistryClicked(
                SelectableLanguage.LanguageUi("language", "ln")
            )
        }
    }

    @Test
    fun when_duplicate_id_error_then_show_error_message() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 0,
                                dictionaryNameCursorIndexEnd = 0,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = true
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            UiEvent(RegistryError.LanguageIdDuplicate)
        )
        every { mockViewModel.onNewLanguageRegistryClicked(any()) } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Add new language")
            .performClick()
        composeTestRule
            .onNodeWithText("id already used, please use another id")
            .assertIsDisplayed()
    }

    @Test
    fun when_click_on_register_then_call_register_on_view_model() {

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = ExternalTableUiInfo(
                                originalTableName = "original table name",
                                dictionaryName = "dictionary name",
                                dictionaryNameCursorIndexStart = 0,
                                dictionaryNameCursorIndexEnd = 0,
                                originalLanguageFrom = "original fr",
                                languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
                                originalLanguageTo = "original en",
                                languageToUi = SelectableLanguage.LanguageUi("English", "en"),
                                isSelected = true
                            ),
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            emptyList()
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.registerDictionaries() } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Register tables")
            .performClick()

        verify {
            mockViewModel.registerDictionaries()
        }

        composeTestRule
            .onNodeWithText("Table imports status")
            .assertIsDisplayed()
    }

    @Test
    fun when_import_in_progress_show_progress_import_dialog() {

        val tableUiInfo = ExternalTableUiInfo(
            originalTableName = "original table name",
            dictionaryName = "dictionary name",
            dictionaryNameCursorIndexStart = 0,
            dictionaryNameCursorIndexEnd = 0,
            originalLanguageFrom = "original fr",
            languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
            originalLanguageTo = "original en",
            languageToUi = SelectableLanguage.LanguageUi("English", "en"),
            isSelected = true
        )

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = tableUiInfo,
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                ),
                importProgress = mapOf(
                    tableUiInfo to DataOperationResult.Success(
                        ImportTableProgress(100, 20)
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            listOf(
                ImportTableStatus(
                    tableUiInfo,
                    DataOperationResult.Success(
                        ImportTableProgress(100, 20)
                    )
                )
            )
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.registerDictionaries() } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Register tables")
            .performClick()

        composeTestRule
            .onNodeWithText("Table imports status")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SemanticTags.importTableProgress)
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.2f, range = 0f..1f))

        composeTestRule
            .onNodeWithText("20/100")
            .assertIsDisplayed()

    }

    @Test
    fun when_import_is_done_then_show_import_success_dialog() {

        val tableUiInfo = ExternalTableUiInfo(
            originalTableName = "original table name",
            dictionaryName = "dictionary name",
            dictionaryNameCursorIndexStart = 0,
            dictionaryNameCursorIndexEnd = 0,
            originalLanguageFrom = "original fr",
            languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
            originalLanguageTo = "original en",
            languageToUi = SelectableLanguage.LanguageUi("English", "en"),
            isSelected = true
        )

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = tableUiInfo,
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                ),
                importProgress = mapOf(
                    tableUiInfo to DataOperationResult.Success(
                        ImportTableProgress(100, 100)
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            listOf(
                ImportTableStatus(
                    tableUiInfo,
                    DataOperationResult.Success(
                        ImportTableProgress(100, 100)
                    )
                )
            )
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.registerDictionaries() } returns Unit

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {},
            )
        }

        composeTestRule
            .onNodeWithText("Register tables")
            .performClick()

        composeTestRule
            .onNodeWithText("Table imports status")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Table original table name is imported successfully as dictionary name.")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SemanticTags.importTableSuccessIcon)
            .assertIsDisplayed()

    }

    @Test
    fun when_clicking_on_query_screen_then_navigate_to_query_screen() {

        val tableUiInfo = ExternalTableUiInfo(
            originalTableName = "original table name",
            dictionaryName = "dictionary name",
            dictionaryNameCursorIndexStart = 0,
            dictionaryNameCursorIndexEnd = 0,
            originalLanguageFrom = "original fr",
            languageFromUi = SelectableLanguage.LanguageUi("French", "fr"),
            originalLanguageTo = "original en",
            languageToUi = SelectableLanguage.LanguageUi("English", "en"),
            isSelected = true
        )

        val mockViewModel = mockk<DictionaryRegistryViewModel>()
        every { mockViewModel.getUiState() } returns MutableLiveData(
            DictionaryRegistryState(
                dbPath = "dbPath",
                registerDictionaryUiModels = LoadableContent.Completed(
                    listOf(
                        RegisterDictionaryUi(
                            tableUiInfo = tableUiInfo,
                            availableLanguages = listOf(
                                SelectableLanguage.LanguageUi("French", "fr"),
                                SelectableLanguage.LanguageUi("English", "en"),
                            )
                        )
                    )
                ),
                importProgress = mapOf(
                    tableUiInfo to DataOperationResult.Success(
                        ImportTableProgress(100, 100)
                    )
                )
            )
        )
        every { mockViewModel.getLiveRegistrationStatus() } returns MutableLiveData(
            listOf(
                ImportTableStatus(
                    tableUiInfo,
                    DataOperationResult.Success(
                        ImportTableProgress(100, 100)
                    )
                )
            )
        )
        every { mockViewModel.registryError } returns MutableLiveData(
            null
        )
        every { mockViewModel.registerDictionaries() } returns Unit

        var isNavigatedBackToQueryScreen = false

        composeTestRule.setContent {
            DictionaryRegistryScreen(
                dbPath = "dbPath",
                viewModel = mockViewModel,
                navigateBack = {},
                navigateToDictionaryQuery = {
                    isNavigatedBackToQueryScreen = true
                },
            )
        }

        composeTestRule
            .onNodeWithText("Register tables")
            .performClick()

        composeTestRule
            .onNodeWithText("Table imports status")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Go back to query screen")
            .performClick()

        assert(isNavigatedBackToQueryScreen)
    }
}
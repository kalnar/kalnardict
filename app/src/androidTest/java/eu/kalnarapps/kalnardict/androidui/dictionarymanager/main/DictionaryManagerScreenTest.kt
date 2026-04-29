package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Environment
import android.os.ParcelFileDescriptor
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.platform.app.InstrumentationRegistry
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.androidui.test.KoinTest
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.android.ext.koin.androidApplication
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class DictionaryManagerScreenTest : KoinTest() {


    @Test
    fun when_no_dictionaries_displayed_show_default_message() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(emptyList())
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("No registered dictionaries found. Please register a dictionary from an external database")
            .assertIsDisplayed()
    }

    @Test
    fun when_there_is_a_registered_dictionary_then_display_item() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    ManageableDictionaryView(
                        dictionaryName = "English French Dictionary",
                        sourceLanguage = "English",
                        destinationLanguage = "French",
                        currentRenderingStrategy = RenderingStrategy("html", "html"),
                        availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                        updateInfo = DictionaryUpdateUi.None,
                        dictionaryId = 1
                    )
                )
            )
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("English French Dictionary")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("English")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("French")
            .assertIsDisplayed()
    }

    @Test
    fun when_clicked_on_a_dictionary_expand_item_with_rendering_strategy() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    ManageableDictionaryView(
                        dictionaryName = "English French Dictionary",
                        sourceLanguage = "English",
                        destinationLanguage = "French",
                        currentRenderingStrategy = RenderingStrategy("html", "html"),
                        availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                        updateInfo = DictionaryUpdateUi.None,
                        dictionaryId = 1
                    )
                )
            )
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("English French Dictionary")
            .performClick()
        composeTestRule
            .onNodeWithText("Update rendering type:")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("html")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Update")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Delete")
            .assertIsDisplayed()
    }

    @Test
    fun when_clicked_on_a_dictionary_rendering_strategy_display_the_list() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    ManageableDictionaryView(
                        dictionaryName = "English French Dictionary",
                        sourceLanguage = "English",
                        destinationLanguage = "French",
                        currentRenderingStrategy = RenderingStrategy("html", "html"),
                        availableRenderingStrategy = listOf(
                            RenderingStrategy("html", "html"),
                            RenderingStrategy("text", "text")
                        ),
                        updateInfo = DictionaryUpdateUi.None,
                        dictionaryId = 1
                    )
                )
            )
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("English French Dictionary")
            .performClick()
        composeTestRule
            .onNodeWithText("html")
            .performClick()
        composeTestRule
            .onAllNodesWithText("html")
            .assertCountEquals(2)
        composeTestRule
            .onNodeWithText("text")
            .assertIsDisplayed()
    }

    @Test
    fun when_clicked_on_a_dictionary_delete_that_view_model_deletes() {
        // Mock the ViewModel
        var isDeleteCalled = false
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    ManageableDictionaryView(
                        dictionaryName = "English French Dictionary",
                        sourceLanguage = "English",
                        destinationLanguage = "French",
                        currentRenderingStrategy = RenderingStrategy("html", "html"),
                        availableRenderingStrategy = listOf(
                            RenderingStrategy("html", "html"),
                            RenderingStrategy("text", "text")
                        ),
                        updateInfo = DictionaryUpdateUi.None,
                        dictionaryId = 1
                    ).apply {
                        onDeleteAction = {
                            isDeleteCalled = true
                        }
                    }
                )
            )
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("English French Dictionary")
            .performClick()
        composeTestRule
            .onNodeWithText("Delete")
            .performClick()

        assertThat(
            isDeleteCalled,
            equalTo(true)
        )
    }

    @Test
    fun when_clicked_on_text_rendering_strategy_and_update_then_update_via_view_model() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(
                listOf(
                    ManageableDictionaryView(
                        dictionaryName = "English French Dictionary",
                        sourceLanguage = "English",
                        destinationLanguage = "French",
                        currentRenderingStrategy = RenderingStrategy("html", "html"),
                        availableRenderingStrategy = listOf(
                            RenderingStrategy("html", "html"),
                            RenderingStrategy("text", "text")
                        ),
                        updateInfo = DictionaryUpdateUi.None,
                        dictionaryId = 1
                    )
                )
            )
        )
        every { mockViewModel.updateDictionary(any()) } returns Unit

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })

        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {},
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("English French Dictionary")
            .performClick()
        composeTestRule
            .onNodeWithText("html")
            .performClick()
        composeTestRule
            .onNodeWithText("text")
            .performClick()
        composeTestRule
            .onNodeWithText("Update")
            .performClick()

        verify {
            mockViewModel.updateDictionary(
                eq(
                    DictionaryUpdateUi.Info(
                        1,
                        RenderingStrategy("text", "text")
                    )
                )
            )
        }
    }

    @Test
    fun when_import_dictionary_display_then_navigate_to_registry() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(emptyList())
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })
        grantViaShell()

        Intents.init()

        try {
            // Stub the intent result BEFORE launching
            val resultIntent = Intent()
            intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)
            )

            var navigatedToRegistry = false
            composeTestRule.setContent {
                DictionaryManagerScreen(
                    viewModel = mockViewModel,
                    navigateToPermissionError = {},
                    navigateToDictionaryRegistry = {
                        navigatedToRegistry = true
                    }
                )
            }

            composeTestRule
                .onNodeWithText("IMPORT NEW DICTIONARIES")
                .performClick()

            intended(hasAction(Intent.ACTION_GET_CONTENT))

            assertEquals(navigatedToRegistry, navigatedToRegistry)

        } finally {
            Intents.release()
        }
    }

    @Test
    fun when_import_dictionary_display_then_navigate_to_permission_error() {
        // Mock the ViewModel
        val mockViewModel = mockk<DictionaryManagerViewModel>()
        every { mockViewModel.getRegisteredDictionaries() } returns MutableLiveData(
            LoadableContent.Completed(emptyList())
        )

        loadKoinModules(module {
            single { UriAdapter(androidApplication()) }
        })
        revokeViaShell()

        var navigatedToPermissionError = false
        composeTestRule.setContent {
            DictionaryManagerScreen(
                viewModel = mockViewModel,
                navigateToPermissionError = {
                    navigatedToPermissionError = true
                },
                navigateToDictionaryRegistry = {}
            )
        }

        composeTestRule
            .onNodeWithText("IMPORT NEW DICTIONARIES")
            .performClick()

        assertEquals(true, navigatedToPermissionError)

    }

    private fun grantViaShell() {
        val packageName = InstrumentationRegistry.getInstrumentation()
            .targetContext.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("appops set $packageName MANAGE_EXTERNAL_STORAGE allow")
            .also { pfd ->
                ParcelFileDescriptor.AutoCloseInputStream(pfd).use { it.readBytes() }
            }

        // Wait and verify
        val granted = waitForCondition(timeOutMs = 3000) {
            Environment.isExternalStorageManager()
        }

        check(granted) { "Failed to grant MANAGE_EXTERNAL_STORAGE permission" }
    }

    private fun revokeViaShell() {
        val packageName = InstrumentationRegistry.getInstrumentation()
            .context.packageName

        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("appops set $packageName MANAGE_EXTERNAL_STORAGE deny")
            .also { pfd ->
                ParcelFileDescriptor.AutoCloseInputStream(pfd).use { it.readBytes() }
            }

        // Wait and verify
        val revoked = waitForCondition(timeOutMs = 3000) {
            !Environment.isExternalStorageManager()
        }

        check(revoked) { "Failed to revoke MANAGE_EXTERNAL_STORAGE permission" }
    }

    private fun waitForCondition(timeOutMs: Long, condition: () -> Boolean): Boolean {
        val deadline = System.currentTimeMillis() + timeOutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return true
            Thread.sleep(100)
        }
        return false
    }

}
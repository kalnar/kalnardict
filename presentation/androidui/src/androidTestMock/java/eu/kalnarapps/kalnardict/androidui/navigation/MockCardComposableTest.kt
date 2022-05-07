package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MockCardComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sampleTest() {

        composeTestRule.setContent {
            MaterialTheme {
                MockCard(dbImporterUi)
            }
        }

        composeTestRule.onNodeWithText("mock db test").performClick()

        composeTestRule.onNodeWithText("ok").assertIsDisplayed()
    }

}
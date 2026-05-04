package eu.kalnarapps.kalnardict.androidui.test

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

abstract class KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun startKoinForTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        startKoin {
            androidContext(context)
        }
    }

    @After
    fun stopKoinAfterTest() {
        stopKoin()
    }
}
package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import eu.kalnarapps.kalnardict.androidui.BaseInstrumentalTest
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.navigationscreen.dictionaryManagerKoinMockModule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class DictionaryManagerScreenTest : BaseInstrumentalTest() {

    @Before
    fun setUp() {
        startKoin {
            modules(
                dictionaryManagerKoinMockModule
            )
        }
        activityRule.activity
            .findNavController(R.id.test_nav_host_fragment)
            .navigate(R.id.dictionaryManager)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun show_fragment_without_crash() {
        Espresso.onView(
            ViewMatchers.withText("mock test dict #1")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(
            ViewMatchers.withText("mock test dict #6")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

}

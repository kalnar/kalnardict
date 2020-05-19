package eu.kalnarapps.kalnardict.androidui

import android.view.View
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionaryManagerKoinModule
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin


class DictionaryManagerScreenTest : BaseInstrumentalTest() {

    @Before
    fun setUp() {
        startKoin {
            modules(
                dictionaryManagerKoinTestModule
            )
        }
        activityRule.activity
            .findNavController(R.id.test_nav_host_fragment)
            .navigate(R.id.dictionaryManager)
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

    @Test
    fun text_fits_in_big_button() {


    }
}

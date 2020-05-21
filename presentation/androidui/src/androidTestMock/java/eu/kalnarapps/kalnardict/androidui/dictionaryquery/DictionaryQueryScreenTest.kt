package eu.kalnarapps.kalnardict.androidui.dictionaryquery

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


class DictionaryQueryScreenTest : BaseInstrumentalTest() {

    @Before
    fun setUp() {
        startKoin {
//            modules(
//            )
        }
        activityRule.activity
            .findNavController(R.id.test_nav_host_fragment)
            .navigate(R.id.dictionaryQuery)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun show_mock_entries() {
        Espresso.onView(
            ViewMatchers.withText("mock test dict entry #1")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(
            ViewMatchers.withText("mock test dict entry #6")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun clear_input_button_displayed() {
        Espresso.onView(
            ViewMatchers.withId(R.id.clear_input_button)
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun language_bar_is_displayed() {
        Espresso.onView(
            ViewMatchers.withText("hu -> fr")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun search_input_is_displayed() {
        Espresso.onView(
            ViewMatchers.withHint("search")
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

}

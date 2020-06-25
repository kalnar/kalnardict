package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import eu.kalnarapps.kalnardict.androidui.BaseInstrumentalTest
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.assertions.RecyclerViewItemCountAssertion
import eu.kalnarapps.kalnardict.androidui.dependencies.dictionaryManagerKoinMockModule
import eu.kalnarapps.kalnardict.androidui.dependencies.dictionaryQueryKoinMockModule
import eu.kalnarapps.kalnardict.androidui.dependencies.mockRepositoryModule
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class DictionaryQueryScreenTest : BaseInstrumentalTest() {

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    mockRepositoryModule,
                    dictionaryManagerKoinMockModule,
                    dictionaryQueryKoinMockModule
                )
            )
        }
        activityRule.activity
            .findNavController(R.id.test_nav_host_fragment)
            .navigate(R.id.dictionaryQueryScreen)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun fragment_is_shown_without_crash() {

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
    fun clear_input_button_is_displayed() {
        Espresso.onView(
            ViewMatchers.withId(R.id.query_clear_input_button)
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun language_bar_is_displayed() {
        Espresso.onView(
            ViewMatchers.withText(containsString("hu -> fr"))
        ).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun filter_entries_with_exact_match() {
        Espresso.onView(
            ViewMatchers.withId(R.id.query_screen_input)
        ).perform(
            ViewActions.typeText("mock test dict entry #2")
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.query_result_list_view)
        ).check(
            RecyclerViewItemCountAssertion(1)
        )
    }

    @Test
    fun filter_entries_with_no_match() {
        Espresso.onView(
            ViewMatchers.withId(R.id.query_screen_input)
        ).perform(
            ViewActions.typeText("anything")
        )

        Espresso.onView(
            ViewMatchers.withId(R.id.query_result_list_view)
        ).check(
            RecyclerViewItemCountAssertion(0)
        )
    }

//
//    @Test
//    fun search_input_is_displayed() {
//        Espresso.onView(
//            ViewMatchers.withHint("search")
//        ).check(
//            ViewAssertions.matches(ViewMatchers.isDisplayed())
//        )
//    }

}

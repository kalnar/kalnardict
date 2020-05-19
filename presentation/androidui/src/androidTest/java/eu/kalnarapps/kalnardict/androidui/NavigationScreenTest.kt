package eu.kalnarapps.kalnardict.androidui

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import eu.kalnarapps.kalnardict.androidui.navigationscreen.TestActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationScreenTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun navigation_graph_contains_dictionary_manager() {
        assertThat(
            activityRule.activity
                .findNavController(R.id.test_nav_host_fragment).graph.iterator()
                .asSequence().toList().map { it.label },
            CoreMatchers.hasItem(
                CoreMatchers.equalTo("Dictionary Manager")
            )
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}

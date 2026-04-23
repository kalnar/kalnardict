package eu.kalnarapps.kalnardict.androidui

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import eu.kalnarapps.kalnardict.androidui.navigationscreen.MockActivity
import org.junit.Rule

@LargeTest
abstract class BaseInstrumentalTest {

    @get:Rule
    val activityRule = ActivityTestRule(MockActivity::class.java)
}
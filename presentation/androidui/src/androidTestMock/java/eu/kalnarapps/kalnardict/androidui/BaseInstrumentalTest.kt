package eu.kalnarapps.kalnardict.androidui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import eu.kalnarapps.kalnardict.androidui.navigationscreen.TestActivity
import org.junit.Rule

@LargeTest
abstract class BaseInstrumentalTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)
}
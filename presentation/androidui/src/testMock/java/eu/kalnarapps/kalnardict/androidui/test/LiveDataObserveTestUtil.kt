package eu.kalnarapps.kalnardict.androidui.test

import com.jraska.livedata.TestObserver
import org.hamcrest.Matcher
import org.junit.Assert.assertThat


fun <T> TestObserver<T>.assertThat(
    matcher: Matcher<in T>
): TestObserver<T> {
    assertThat<T>(
        value(),
        matcher
    )
    return this
}
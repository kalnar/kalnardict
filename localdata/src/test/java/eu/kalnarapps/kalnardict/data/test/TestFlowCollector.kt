package eu.kalnarapps.kalnardict.data.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection

fun <T> Flow<T>.test(scope: CoroutineScope): TestObserver<T> {
    return TestObserver(scope, this)
}

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private val job: Job = scope.launch {
        flow.collect { values.add(it) }
    }

    fun <P> assertThat(
        actualTransformation: (List<T>) -> P,
        expected: Matcher<P>
    ): TestObserver<T> {
        assertThat(actualTransformation(values), expected)
        return this
    }

    fun assertThatLastValue(
        expected: Matcher<T>
    ): TestObserver<T> {
        assertThat(values, not(IsEmptyCollection()))
        assertThat(values.last(), expected)
        return this
    }


    fun finish() {
        job.cancel()
    }
}
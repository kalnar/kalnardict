package eu.kalnarapps.kalnardict.data.android.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> Flow<T>.test(scope: CoroutineScope): TestObserver<T> {
    return TestObserver(scope, flow = this)
}

fun <T> Flow<T>.test(scope: CoroutineScope, dispatcher: CoroutineContext): TestObserver<T> {
    return TestObserver(scope,  this, dispatcher)
}

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>,
    dispatcher: CoroutineContext = EmptyCoroutineContext
) {
    private val values = mutableListOf<T>()
    private val job: Job = scope.launch(dispatcher) {
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
        assertThat(values.last(), expected)
        return this
    }

    suspend fun join() {
        job.join()
    }

    fun finish() {
        job.cancel()
    }
}
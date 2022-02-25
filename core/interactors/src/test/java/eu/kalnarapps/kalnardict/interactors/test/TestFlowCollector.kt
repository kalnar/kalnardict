package eu.kalnarapps.kalnardict.interactors.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

fun <T> Flow<T?>.test(scope: CoroutineScope): TestObserver<T> {
    return TestObserver(scope, this)
}

class NoEmissionException(msg: String) : Exception(msg)

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T?>
) {
    private val values = mutableListOf<T>()
    private val job: Job = scope.launch {
        flow.collect {
            if (it == null) {
                throw NoEmissionException("scope is null, check if flow emits correctly")
            } else {
                values.add(it)
            }
        }
    }

    fun <P> assertThat(
        actualTransformation: (List<T>) -> P,
        expected: Matcher<P>
    ): TestObserver<T> {
        try {
            assertThat(actualTransformation(values), expected)
        } catch (e: Throwable) {
            job.cancel()
            throw e
        }
        return this
    }

    fun assertThatLastValue(
        expected: Matcher<T>
    ): TestObserver<T> {
        try {
            assertThat(values.last(), expected)
        } catch (e: Throwable) {
            job.cancel()
            throw e
        }
        return this
    }


    fun finish() {
        job.cancel()
    }
}
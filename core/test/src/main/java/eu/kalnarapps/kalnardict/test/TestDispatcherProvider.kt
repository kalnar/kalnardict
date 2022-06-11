package eu.kalnarapps.kalnardict.test

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher


object TestDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

    override fun main(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

}

class TestCoroutineDispatcherProvider(private val coroutineRule: TestCoroutineRule): DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return coroutineRule.testCoroutineDispatcher
    }

    override fun main(): CoroutineDispatcher {
        return coroutineRule.testCoroutineDispatcher
    }
}


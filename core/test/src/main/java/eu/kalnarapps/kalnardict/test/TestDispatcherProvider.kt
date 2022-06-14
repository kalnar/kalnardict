package eu.kalnarapps.kalnardict.test

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher


class TestCoroutineDispatcherProvider(private val coroutineRule: TestCoroutineRule): DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return coroutineRule.testCoroutineDispatcher
    }

    override fun main(): CoroutineDispatcher {
        return coroutineRule.testCoroutineDispatcher
    }
}


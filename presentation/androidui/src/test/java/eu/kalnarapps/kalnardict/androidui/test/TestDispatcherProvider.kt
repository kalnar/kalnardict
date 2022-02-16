package eu.kalnarapps.kalnardict.androidui.test

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher


object TestDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

    override fun main(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

}


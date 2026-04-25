package eu.kalnarapps.kalnardict.test

import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher


class TestCoroutineDispatcherProvider(private val dispatcher: CoroutineDispatcher): DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return dispatcher
    }

    override fun main(): CoroutineDispatcher {
        return dispatcher
    }
}


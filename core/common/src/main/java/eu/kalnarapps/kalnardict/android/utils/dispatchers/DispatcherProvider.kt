package eu.kalnarapps.kalnardict.android.utils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


object DefaultDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}


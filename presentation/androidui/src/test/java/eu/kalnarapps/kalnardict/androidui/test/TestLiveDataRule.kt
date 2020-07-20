package eu.kalnarapps.kalnardict.androidui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestLiveDataRule<T> : TestRule {

    private val observerRegistry = mutableMapOf<LiveData<T>, Observer<T>>()

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {

            try {
                base.evaluate()
            } finally {
                releaseObservers()
            }

        }
    }

    public fun registerObserver(observable: LiveData<T>, observer: Observer<T>) {
        observable.observeForever(observer)
        observerRegistry[observable] = observer
    }

    private fun releaseObservers() {
        for (observingRegistryEntry in observerRegistry) {
            observingRegistryEntry.key.removeObserver(observingRegistryEntry.value)
        }
    }

}


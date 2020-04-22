package eu.kalnarapps.coroutines

import kotlinx.coroutines.runBlocking
import org.junit.Test


class MyTest {
    @Test
    fun testMySuspendingFunction() = runBlocking<Unit> {
        // here we can use suspending functions using any assertion style that we like
    }
}


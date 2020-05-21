package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.androidui.dependencies.dictionaryManagerKoinMockModule
import eu.kalnarapps.kalnardict.androidui.dependencies.dictionaryQueryMockKoinModule
import eu.kalnarapps.kalnardict.androidui.dependencies.dictionaryRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KalnarDictMockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictMockApplication)
            modules(dataModules)
            modules(
                listOf(
                    dictionaryRepositoryModule,
                    dictionaryManagerKoinMockModule,
                    dictionaryQueryMockKoinModule
                )
            )
        }
    }

}
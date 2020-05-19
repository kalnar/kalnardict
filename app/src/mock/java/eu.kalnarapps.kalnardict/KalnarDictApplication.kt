package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.androidui.navigationscreen.dictionaryManagerKoinMockModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KalnarDictMockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictMockApplication)
            modules(dataModules)
            modules(dictionaryManagerKoinMockModule)
        }
    }

}
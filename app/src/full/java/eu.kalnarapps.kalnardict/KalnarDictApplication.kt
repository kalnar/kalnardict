package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionaryManagerKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class KalnarDictApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictApplication)
            modules(dataModules)
            modules(dictionaryManagerKoinModule)
        }
    }

}
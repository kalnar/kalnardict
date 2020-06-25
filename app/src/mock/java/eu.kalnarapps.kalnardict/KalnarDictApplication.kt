package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.androidui.dependencies.androidUiKoinMockModules
import eu.kalnarapps.kalnardict.androidui.dependencies.mockRepositoryModule
import eu.kalnarapps.kalnardict.koin.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KalnarDictMockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictMockApplication)
            modules(dataModules)
            modules(mockRepositoryModule)
            modules(useCaseModule)
            modules(androidUiKoinMockModules)
        }
    }

}
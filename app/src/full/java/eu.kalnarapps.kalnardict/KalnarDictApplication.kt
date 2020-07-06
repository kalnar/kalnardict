package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.koin.dataModule
import eu.kalnarapps.kalnardict.koin.navigationKoinModule
import eu.kalnarapps.kalnardict.koin.repositoryModule
import eu.kalnarapps.kalnardict.koin.useCaseModule
import eu.kalnarapps.kalnardict.koin.utilsModule
import eu.kalnarapps.kalnardict.koin.viewModuleModule
import eu.kalnarapps.kalnardict.koin.mapperModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class KalnarDictApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictApplication)
            modules(navigationKoinModule)
            modules(dataModule)
            modules(repositoryModule)
            modules(useCaseModule)
            modules(utilsModule)
            modules(mapperModule)
            modules(viewModuleModule)
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
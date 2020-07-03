package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.koin.dataModule
import eu.kalnarapps.kalnardict.koin.navigationKoinModule
import eu.kalnarapps.kalnardict.koin.repositoryModule
import eu.kalnarapps.kalnardict.koin.useCaseModule
import eu.kalnarapps.kalnardict.koin.utilsModule
import eu.kalnarapps.kalnardict.koin.viewModuleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class KalnarDictApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictApplication)
            modules(dataModule)
            modules(repositoryModule)
            modules(useCaseModule)
            modules(navigationKoinModule)
            modules(utilsModule)
            modules(viewModuleModule)
        }
    }

}
package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.koin.dataModule
import eu.kalnarapps.kalnardict.koin.mapperModule
import eu.kalnarapps.kalnardict.koin.queryExecutorModule
import eu.kalnarapps.kalnardict.koin.repositoryModule
import eu.kalnarapps.kalnardict.koin.stringResolverModule
import eu.kalnarapps.kalnardict.koin.useCaseForUiModule
import eu.kalnarapps.kalnardict.koin.useCaseFromUiModule
import eu.kalnarapps.kalnardict.koin.useCaseModule
import eu.kalnarapps.kalnardict.koin.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class KalnarDictApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictApplication)
            modules(stringResolverModule)
            modules(mapperModule)
            modules(dataModule)
            modules(queryExecutorModule)
            modules(repositoryModule)
            modules(utilsModule)
            modules(useCaseModule)
            modules(useCaseForUiModule)
            modules(useCaseFromUiModule)
        }
        //  uncomment if you want to use stetho
        //  if (BuildConfig.DEBUG) {
        //      Stetho.initializeWithDefaults(this)
        //  }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
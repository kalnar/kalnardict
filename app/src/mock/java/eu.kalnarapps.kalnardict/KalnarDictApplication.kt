package eu.kalnarapps.kalnardict

import android.app.Application
import eu.kalnarapps.kalnardict.androidui.dependencies.androidUiKoinMockModules
import eu.kalnarapps.kalnardict.androidui.dependencies.mockRepositoryModule
import eu.kalnarapps.kalnardict.androidui.dependencies.navigationScreenKoinMockModule
import eu.kalnarapps.kalnardict.di.useCaseMockModule
import eu.kalnarapps.kalnardict.koin.dataModule
import eu.kalnarapps.kalnardict.koin.mapperModule
import eu.kalnarapps.kalnardict.koin.mockRepositoryKoinModule
import eu.kalnarapps.kalnardict.koin.mockUseCaseModule
import eu.kalnarapps.kalnardict.koin.navigationKoinModule
import eu.kalnarapps.kalnardict.koin.queryExecutorModule
import eu.kalnarapps.kalnardict.koin.repositoryModule
import eu.kalnarapps.kalnardict.koin.stringResolverModule
import eu.kalnarapps.kalnardict.koin.useCaseForUiModule
import eu.kalnarapps.kalnardict.koin.useCaseFromUiModule
import eu.kalnarapps.kalnardict.koin.useCaseModule
import eu.kalnarapps.kalnardict.koin.utilsModule
import eu.kalnarapps.kalnardict.koin.viewModule
import eu.kalnarapps.kalnardict.koin.viewModuleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KalnarDictMockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KalnarDictMockApplication)
            modules(navigationScreenKoinMockModule)
            modules(navigationKoinModule)
            modules(stringResolverModule)
            modules(mapperModule)
            modules(dataModule)
            modules(queryExecutorModule)
            modules(repositoryModule)
            modules(utilsModule)
            modules(useCaseModule)
            modules(useCaseForUiModule)
            modules(useCaseFromUiModule)
            modules(viewModuleModule)
            modules(viewModule)
        }
    }

}
package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.android.utils.KalnarLogger
import eu.kalnarapps.kalnardict.android.utils.Logger
import org.koin.core.module.Module
import org.koin.dsl.module


val utilsModule: Module = module {
    single { KalnarLogger() as Logger }
}
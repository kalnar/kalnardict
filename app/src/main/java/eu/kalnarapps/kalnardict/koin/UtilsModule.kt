package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.android.utils.KalnarUiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import org.koin.core.module.Module
import org.koin.dsl.module


val utilsModule: Module = module {
    single { KalnarUiLogger() as UiLogger }
    single { UriAdapter() }
}
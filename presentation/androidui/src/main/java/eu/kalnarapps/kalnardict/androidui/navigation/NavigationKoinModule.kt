package eu.kalnarapps.kalnardict.androidui.navigation

import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module


val navigationKoinModule = module {
    single {
        AndroidScreenNavigator() as ScreenNavigator
    }
}

object Navigation {
    private const val NAV_CONTROLLER_ID = "kalnardict_nav_controller"
    val navControllerQualifier = StringQualifier(NAV_CONTROLLER_ID)
}
package eu.kalnarapps.kalnardict.koin

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.navigation.AndroidScreenNavigator
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.ScreenNavigator
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val navigationKoinModule = module {
    factory {(navController: NavController) ->
        AndroidScreenNavigator(navController) as ScreenNavigator
    }
}

object Navigation {
    private const val NAV_CONTROLLER_ID = "kalnardict_nav_controller"
    val navControllerQualifier = StringQualifier(NAV_CONTROLLER_ID)
}

package eu.kalnarapps.kalnardict.koin

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.navigation.AndroidPlatformNavigator
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

fun androidPlatformNavigationBean(module: Module): KoinDefinition<PlatformNavigator> {
    return module.factory { (navController: NavController) ->
        AndroidPlatformNavigator(navController) as PlatformNavigator
    }
}

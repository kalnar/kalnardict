package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.androidui.navigation.AndroidPlatformNavigator
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import org.koin.core.definition.BeanDefinition
import org.koin.core.module.Module

fun androidPlatformNavigationBean(module: Module): BeanDefinition<PlatformNavigator> {
    return module.factory {
        AndroidPlatformNavigator() as PlatformNavigator
    }
}

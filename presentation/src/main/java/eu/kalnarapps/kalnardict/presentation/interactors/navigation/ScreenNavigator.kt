package eu.kalnarapps.kalnardict.presentation.interactors.navigation

import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand

interface ScreenNavigator {

    fun execute(navigationCommand: NavigationCommand)
}

interface PlatformNavigator {
    fun navigate(navigationCommand: NavigationCommand.Platform)
}
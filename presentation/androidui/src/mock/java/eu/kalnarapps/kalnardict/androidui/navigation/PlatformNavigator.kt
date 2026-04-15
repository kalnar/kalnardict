package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import  org.koin.core.component.KoinComponent

class AndroidPlatformNavigator(
    private val navController: NavController
) : PlatformNavigator, KoinComponent {

    override fun navigate(navigationCommand: NavigationCommand.Platform) {
        when (navigationCommand) {
            NavigationCommand.Platform.NavigateToDbBrowser -> {
                navController.navigate(R.id.mockDbCreator)
            }
        }.exhaustive
    }

}
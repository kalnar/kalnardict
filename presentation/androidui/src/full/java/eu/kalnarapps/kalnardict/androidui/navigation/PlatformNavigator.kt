package eu.kalnarapps.kalnardict.androidui.navigation

import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidPlatformNavigator : PlatformNavigator, KoinComponent {

    private val navigateToDbBrowser: (NavigationCommand.Platform.NavigateToDbBrowser) -> Unit by inject()

    override fun navigate(navigationCommand: NavigationCommand.Platform) {
        when (navigationCommand) {
            is NavigationCommand.Platform.NavigateToDbBrowser -> {
                navigateToDbBrowser.invoke(navigationCommand)
            }
        }.exhaustive
    }

}
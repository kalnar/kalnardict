package eu.kalnarapps.kalnardict.androidui.navigation

import android.app.Application
import android.content.Intent
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import org.koin.core.KoinComponent
import org.koin.core.inject

class AndroidPlatformNavigator : PlatformNavigator, KoinComponent {

    private val application : Application by inject()

    override fun navigate(navigationCommand: NavigationCommand.Platform) {
        when (navigationCommand) {
            NavigationCommand.Platform.NavigateToDbBrowser -> {
                val intent = Intent(application, MockDbImportActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                application.startActivity(intent)
            }
        }.exhaustive
    }

}
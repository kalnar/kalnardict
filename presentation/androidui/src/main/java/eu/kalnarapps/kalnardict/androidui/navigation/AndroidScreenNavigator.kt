package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryFragmentArgs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog.DictionaryRegistrationStatusDialogArgs
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.PlatformNavigator
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import org.koin.core.KoinComponent

class AndroidScreenNavigator(
    private val navController: NavController,
    private val platformNavigator: PlatformNavigator
) : ScreenNavigator, KoinComponent {

    override fun execute(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is NavigationCommand.Common -> when (navigationCommand) {
                NavigationCommand.Common.Back -> navigateBack()
                is NavigationCommand.Common.NavigateToDictionaryRegistry -> {
                    navigateToDictionaryRegistry(navigationCommand.uri)
                }
                is NavigationCommand.Common.NavigateToDictionaryRegistryDialog -> {
                    navigateToDictionaryRegistryDialog(navigationCommand.uri)
                }
                NavigationCommand.Common.NavigateToDictionaryRegistryNewLanguageDialog -> {
                    showLanguageRegistryDialog()
                }
                NavigationCommand.Common.NavigateToDictionaryQuery -> navigateToDictionaryQuery()
                NavigationCommand.Common.NavigateToDictionaryTranslation -> navigateToDictionaryTranslation()
                NavigationCommand.Common.NavigateToDictionaryManager -> navigateToDictionaryManager()
                NavigationCommand.Common.DoNothing -> Unit
            }
            is NavigationCommand.Platform -> platformNavigator.navigate(navigationCommand)
        }.exhaustive
    }

    private fun showLanguageRegistryDialog() {
        navController.navigate(
            R.id.newLanguageDialog
        )
    }

    private fun navigateToDictionaryManager() {
        navController.navigate(
            R.id.dictionaryManager
        )
    }

    private fun navigateToDictionaryRegistry(dbUri: String) {
        navController.navigate(
            R.id.dictionary_registration_navigation,
            DictionaryRegistryFragmentArgs(dbPath = dbUri).toBundle()
        )
    }

    private fun navigateToDictionaryRegistryDialog(uri: String) {
        navController.navigate(
            R.id.dictionaryRegistrationStatusDialog,
            DictionaryRegistrationStatusDialogArgs(dbPath = uri).toBundle()
        )
    }

    private fun navigateBack() {
        navController.popBackStack()
        return
    }

    private fun navigateToDictionaryQuery() {
        navController.let {
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(it.graph.startDestination, false)
//                .setPopUpTo(R.id.navigation_screen, false)
                .build()
            it.navigate(
                R.id.dictionary_query_navigation,
                null,
                navOptions
            )
        }
    }

    private fun navigateToDictionaryTranslation() {
        val navigationOccurred = navController.popBackStack(
            R.id.dictionaryTranslationScreen,
            false
        )

        if (!navigationOccurred) {
            navController.navigate(
                R.id.dictionaryTranslationScreen,
                null
            )
        }
    }

}
package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dialogs.FailedTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dialogs.SuccessTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryFragmentArgs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog.DictionaryRegistrationStatusDialogArgs
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import org.koin.core.KoinComponent

class AndroidScreenNavigator(
    private val navController: NavController
) : ScreenNavigator, KoinComponent {

    override fun execute(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            NavigationCommand.Back -> navigateBack()
            is NavigationCommand.NavigateToDictionaryRegistry -> {
                navigateToDictionaryRegistry(navigationCommand.uri)
            }
            is NavigationCommand.NavigateToDictionaryRegistryDialog -> {
                navigateToDictionaryRegistryDialog(navigationCommand.uri)
            }
            NavigationCommand.NavigateToDictionaryRegistryNewLanguageDialog -> {
                showLanguageRegistryDialog()
            }
            NavigationCommand.NavigateToDictionaryQuery -> navigateToDictionaryQuery()
            NavigationCommand.NavigateToDictionaryTranslation -> navigateToDictionaryTranslation()
            NavigationCommand.NavigateToDictionaryManager -> navigateToDictionaryManager()
            is NavigationCommand.ShowDialog -> showDialog(navigationCommand)
            NavigationCommand.DoNothing -> Unit
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

    private fun showDialog(navigationCommand: NavigationCommand.ShowDialog) {
        when (navigationCommand) {
            is NavigationCommand.ShowDialog.SuccessTableRegistration -> {
                navController.navigate(
                    R.id.successTableRegistrationDialog,
                    SuccessTableRegistrationDialogArgs(
                        originalTableName = navigationCommand.table,
                        dictionaryName = navigationCommand.dictionaryName
                    ).toBundle()
                )
            }
            is NavigationCommand.ShowDialog.FailureTableRegistration -> {
                navController.navigate(
                    R.id.failedTableRegistrationDialog,
                    FailedTableRegistrationDialogArgs(
                        originalTableName = navigationCommand.table,
                        errorMessage = navigationCommand.errorMessage
                    ).toBundle()
                )
            }
        }.exhaustive
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
        // TODO
    }

}
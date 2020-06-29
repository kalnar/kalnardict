package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dialogs.FailedTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dialogs.SuccessTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryFragmentArgs
import eu.kalnarapps.kalnardict.androidui.navigationscreen.NavigationScreenDirections
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import org.koin.core.KoinComponent
import java.net.URI

class AndroidScreenNavigator : ScreenNavigator, KoinComponent {
    private val navController: NavController?
        get() {
            return getKoin().getProperty(Navigation.navControllerQualifier.value)
        }

    override fun execute(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            NavigationCommand.Back -> navigateBack()
            is NavigationCommand.NavigateToDictionaryRegistry -> navigateToDictionaryRegistry(
                navigationCommand.uri
            )
            NavigationCommand.NavigateToDictionaryQuery -> navigateToDictionaryQuery()
            NavigationCommand.NavigateToDictionaryManager -> navigateToDictionaryManager()
            is NavigationCommand.ShowDialog -> showDialog(navigationCommand)
        }.exhaustive
    }

    private fun navigateToDictionaryManager() {
        navController?.navigate(
            NavigationScreenDirections.navigateToDictionaryManager()
        )
    }

    private fun navigateToDictionaryRegistry(dbUri: URI) {
        navController?.navigate(
            R.id.dictionaryRegistry,
            DictionaryRegistryFragmentArgs(dbPath = dbUri.path).toBundle()
        )
    }


    private fun showDialog(navigationCommand: NavigationCommand.ShowDialog) {
        when (navigationCommand) {
            is NavigationCommand.ShowDialog.SuccessTableRegistration -> {
                navController?.navigate(
                    R.id.successTableRegistrationDialog,
                    SuccessTableRegistrationDialogArgs(
                        originalTableName = navigationCommand.table,
                        dictionaryName = navigationCommand.dictionaryName
                    ).toBundle()
                )
            }
            is NavigationCommand.ShowDialog.FailureTableRegistration -> {
                navController?.navigate(
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
        navController?.popBackStack()
        return
    }

    private fun navigateToDictionaryQuery() {
        navController?.navigate(
            R.id.dictionaryQueryScreen
        )
    }
}
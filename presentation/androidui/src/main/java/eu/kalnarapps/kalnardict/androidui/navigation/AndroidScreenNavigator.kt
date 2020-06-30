package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dialogs.FailedTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dialogs.SuccessTableRegistrationDialogArgs
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryFragmentArgs
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import org.koin.core.KoinComponent
import java.net.URI

class AndroidScreenNavigator() : ScreenNavigator, KoinComponent {
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
            NavigationCommand.DoNothing -> Unit
        }.exhaustive
    }

    private fun navigateToDictionaryManager() {
        navController?.navigate(
            R.id.dictionaryManager
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
        navController?.let {
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(it.graph.startDestination, true)
                .build()
            it.navigate(
                R.id.dictionaryQueryScreen,
                null,
                navOptions
            )
        }
    }
}
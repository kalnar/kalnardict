package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerScreen
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.DictionaryRegistryScreen
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryScreen
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModel
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryQueryViewModelFactory
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionaryTranslationScreen
import eu.kalnarapps.kalnardict.androidui.permission.PermissionErrorScreen

fun NavGraphBuilder.internalAppNavHost(navController: NavHostController) {
    composable(Screen.DictionaryQuery.route) {
        DictionaryQueryScreen(
            navigateToDictionaryManager = {
                navController.navigate(Screen.DictionaryManager.route)
            },
            navigateToTranslation = {
                navController.navigate(Screen.DictionaryTranslation.route)
            }
        )
    }
    composable(Screen.DictionaryTranslation.route) { backStackEntry ->
        val queryEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.DictionaryQuery.route)
        }
        val sharedViewModel = viewModel<DictionaryQueryViewModel>(
            viewModelStoreOwner = queryEntry,
            factory = DictionaryQueryViewModelFactory()
        )
        DictionaryTranslationScreen(viewModel = sharedViewModel)
    }
    composable(Screen.PermissionError.route) {
        PermissionErrorScreen(navController = navController)
    }
    composable(Screen.DictionaryManager.route) {
        DictionaryManagerScreen(
            navigateToDictionaryRegistry = { dbPath ->
                navController.navigate(
                    Screen.DictionaryRegistry(dbPath)
                )
            },
            navigateToPermissionError = {
                navController.navigate(Screen.PermissionError.route)
            }
        )
    }
    composable<Screen.DictionaryRegistry> { backStackEntry ->
        val route: Screen.DictionaryRegistry = backStackEntry.toRoute()
        DictionaryRegistryScreen(
            dbPath = route.dbPath,
            navigateBack = {
                navController.popBackStack()
            },
            navigateToDictionaryQuery = {
                navController.navigate(
                    Screen.DictionaryQuery.route,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )
            },
        )
    }
}

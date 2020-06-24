package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerFragmentDirections
import java.net.URI

class AndroidScreenNavigator(
    private val navController: NavController
) : ScreenNavigator {
    override fun navigateToDictionaryRegistry(dbUri: URI) {
        navController.navigate(
            DictionaryManagerFragmentDirections.navigateToDictionaryRegistry(
                dbPath = dbUri.path
            )
        )
    }
}
package eu.kalnarapps.kalnardict.androidui.navigation

import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.DictionaryManagerFragmentDirections
import org.koin.core.KoinComponent
import java.net.URI

class AndroidScreenNavigator : ScreenNavigator, KoinComponent {
    private val navController: NavController?
        get() {
            return getKoin().getProperty(Navigation.navControllerQualifier.value)
        }

    override fun navigateToDictionaryRegistry(dbUri: URI) {
        navController?.navigate(
            DictionaryManagerFragmentDirections.navigateToDictionaryRegistry(
                dbPath = dbUri.path
            )
        )
    }
}
package eu.kalnarapps.kalnardict

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.presentation.interactors.navigation.ScreenNavigator
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {

    private val uriAdapter: UriAdapter by inject()
    private val navigator: ScreenNavigator by inject {
        parametersOf(findNavController(R.id.main_nav_host_fragment))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        loadKoinModules(platformNavigationModule)
    }

    override fun onStop() {
        super.onStop()
        unloadKoinModules(platformNavigationModule)
    }

    private val dbBrowserLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // Warning: navigator is initialized after onCreate of main activity
        navigator.execute(
            NavigationCommand.Common.NavigateToDictionaryRegistry(
                uriAdapter.convertUriToSdcardPath(uri)
            )
        )
    }

    private val platformNavigationModule = module {
        single {
            { _: NavigationCommand.Platform.NavigateToDbBrowser ->
                dbBrowserLauncher.launch(
                    "*/*"
                )
            } as ((NavigationCommand.Platform.NavigateToDbBrowser) -> Unit)
        }
    }

    override fun onBackPressed() {
        findNavController(R.id.main_nav_host_fragment).apply {
            val lastNavigationItemId = this.currentDestination?.id
            popBackStack().let {
                if (!it) {
                    if (lastNavigationItemId == R.id.dictionaryQueryScreen) {
                        this@MainActivity.finish()
                    } else {
                        this.navigate(
                            R.id.dictionary_query_navigation,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.navigation_screen, true)
                                .build()
                        )
                    }
                }
            }
        }
    }

}

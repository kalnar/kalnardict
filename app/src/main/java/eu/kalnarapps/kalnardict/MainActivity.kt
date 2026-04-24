package eu.kalnarapps.kalnardict

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.androidui.navigation.Screen
import eu.kalnarapps.kalnardict.androidui.navigation.internalAppNavHost
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val uriAdapter: UriAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MainActivityViewModel =
                        viewModel(factory = MainActivityViewModelFactory())

                    if (!hasManageExternalStoragePermission()) {
                        AppNavHost(
                            navController = navController,
                            startDestination = Screen.PermissionError.route
                        )
                        return@Surface
                    }

                    val hasDictionaries by viewModel.hasDictionaries.collectAsState()

                    when (val resolved = hasDictionaries) {
                        null -> Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        else -> AppNavHost(
                            navController = navController,
                            startDestination = if (resolved) Screen.DictionaryQuery.route else Screen.DictionaryManager.route
                        )
                    }
                }
            }
        }
    }

    private fun hasManageExternalStoragePermission(): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()).also {
            Log.d("MainActivityV2", "hasManageExternalStoragePermission: $it")
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        internalAppNavHost(navController)
    }
}

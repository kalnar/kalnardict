package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.kalnarapps.kalnardict.androidui.importer.DbImporterViewModel
import eu.kalnarapps.kalnardict.androidui.navigation.DbImporterViewModelFactory
import eu.kalnarapps.kalnardict.androidui.navigation.MockDbScreen
import eu.kalnarapps.kalnardict.androidui.navigation.Screen
import eu.kalnarapps.kalnardict.androidui.navigation.internalAppNavHost
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.presentation.models.mock.DbImporterUi

private const val ROUTE_MOCK_HOME = "mock_home"
private const val ROUTE_MOCK_DB_IMPORT = "mock_db_import"

class MockActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MockNavHost(navController)
                }
            }
        }
    }
}

@Composable
private fun MockNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ROUTE_MOCK_HOME) {
        composable(ROUTE_MOCK_HOME) {
            MockHomeScreen(navController)
        }
        composable(ROUTE_MOCK_DB_IMPORT) {
            val dbImporterViewModel: DbImporterViewModel =
                viewModel(factory = DbImporterViewModelFactory())
            val state by dbImporterViewModel.getUiState().observeAsState(DbImporterUi())
            LaunchedEffect(Unit) {
                dbImporterViewModel.navigationEvent.collect {
                    navController.navigate(
                        Screen.DictionaryRegistry(it),
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(0, true)
                            .build()
                    )
                }
            }
            MockDbScreen(state, dbImporterViewModel::onFormValidation)
        }
        internalAppNavHost(navController)
    }
}

@Composable
private fun MockHomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {

            item {
                MockNavButton("PermissionScreen") {
                    navController.navigate(Screen.PermissionError.route)
                }
            }
            item {
                MockNavButton("DictionaryQuery") {
                    navController.navigate(Screen.DictionaryQuery.route)
                }
            }
            item {
                MockNavButton("DictionaryManager") {
                    navController.navigate(Screen.DictionaryManager.route)
                }
            }
            item {
                MockNavButton("MockDbImport") {
                    navController.navigate(ROUTE_MOCK_DB_IMPORT)
                }
            }
            item {
                MockNavButton("DictionaryRegistry - invalid db") {
                    navController.navigate(Screen.DictionaryRegistry(dbPath = UiStubs.Uris.invalidUri))
                }
            }
        }
    }
}

@Composable
private fun MockNavButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(label)
    }
}

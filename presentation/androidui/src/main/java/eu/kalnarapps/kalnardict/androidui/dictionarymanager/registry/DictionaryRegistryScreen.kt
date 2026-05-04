package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose.ImportStatusDialog
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose.NewLanguageDialog
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose.TableInfoItem
import eu.kalnarapps.kalnardict.androidui.navigation.Screen
import eu.kalnarapps.kalnardict.androidui.theme.ColorPrimaryLight
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegistryError
import kotlinx.coroutines.launch

@Composable
fun DictionaryRegistryScreen(
    dbPath: String,
    viewModel: DictionaryRegistryViewModel = viewModel(
        factory = DictionaryRegistryViewModelComposeFactory(dbPath)
    ),
    navigateBack: () -> Unit,
    navigateToDictionaryQuery: () -> Unit,
) {
    val uiState by viewModel.getUiState().observeAsState()
    val importStatus by viewModel.getLiveRegistrationStatus().observeAsState(emptyList())
    val registryErrorEvent by viewModel.registryError.observeAsState()

    var showImportDialog by remember { mutableStateOf(false) }
    var showNewLanguageDialog by remember { mutableStateOf(false) }
    var duplicateIdError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Consume registry error events (one-shot, like UiEventObserver)
    LaunchedEffect(registryErrorEvent) {
        val event = registryErrorEvent ?: return@LaunchedEffect
        if (!event.hasBeenHandled()) {
            when (event.content()) {
                RegistryError.LanguageIdDuplicate -> duplicateIdError = true
            }
        }
    }

    val registerModels = uiState?.registerDictionaryUiModels

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = ColorPrimaryLight,
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                registerModels == null ||
                        registerModels == LoadableContent.UnInitialized ||
                        registerModels == LoadableContent.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                registerModels is LoadableContent.Failed -> {
                    Text(
                        text = "Invalid database file selected. No dictionaries found.",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(40.dp)
                    )
                    LaunchedEffect(Unit) {
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "An error has occurred. Go back to dictionary manager.",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Indefinite,
                            )

                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    navigateBack.invoke()
                                }

                                SnackbarResult.Dismissed -> {
                                    // Snackbar was dismissed without action
                                }
                            }
                        }
                    }
                }

                registerModels is LoadableContent.Completed -> {
                    LazyColumn {
                        items(
                            items = registerModels.content,
                            key = { it.tableUiInfo.originalTableName }
                        ) { registerDictionaryUi ->
                            TableInfoItem(
                                registerDictionaryUi = registerDictionaryUi,
                                onTableInfoChanged = { viewModel.onTableRegisteringUpdate(it) }
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = { showNewLanguageDialog = true }
                                ) {
                                    Text("Add new language")
                                }
                                Button(
                                    onClick = {
                                        showImportDialog = true
                                        viewModel.registerDictionaries()
                                    },
                                    enabled = registerModels.content.any { it.tableUiInfo.isSelected }
                                ) {
                                    Text("Register tables")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showImportDialog) {
        ImportStatusDialog(
            importStatus = importStatus,
            onGoToQueryScreen = {
                showImportDialog = false
                navigateToDictionaryQuery.invoke()
            }
        )
    }

    if (showNewLanguageDialog) {
        NewLanguageDialog(
            duplicateIdError = duplicateIdError,
            onRegister = {
                duplicateIdError = false
                viewModel.onNewLanguageRegistryClicked(it)
            },
            onDismiss = {
                duplicateIdError = false
                showNewLanguageDialog = false
            }
        )
    }
}

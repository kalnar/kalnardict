package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.compose.DictionaryDropdown
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.compose.WordItem
import eu.kalnarapps.kalnardict.androidui.navigation.Screen
import eu.kalnarapps.kalnardict.androidui.theme.ColorPrimary
import eu.kalnarapps.kalnardict.androidui.theme.ColorPrimaryDarkButton
import eu.kalnarapps.kalnardict.androidui.theme.ColorWhite
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryQueryScreen(
    viewModel: DictionaryQueryViewModel = viewModel(factory = DictionaryQueryViewModelFactory()),
    navController: NavController,
) {
    val queryResult by viewModel.getQueryResult().observeAsState()
    val dictionaries by viewModel.getRegisteredDictionaries().observeAsState()
    val queryModes by viewModel.getQueryModes().observeAsState(emptyList())

    val queryText = viewModel.typedQuery.collectAsStateWithLifecycle()

    var queryModesExpanded by remember { mutableStateOf(false) }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "kalnarDict",
                        color = ColorWhite,
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = ColorWhite,
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Dictionary Manager") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Screen.DictionaryManager.route)
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 10.dp),
                colors = TopAppBarDefaults
                    .topAppBarColors(containerColor = ColorPrimary),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
        ) {
            // Search bar (60dp height): text input + query mode button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = queryText.value,
                    onValueChange = {
                        viewModel.onQueryChanged(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 6.dp),
                    placeholder = { Text("Search for word") },
                    trailingIcon = {
                        if (queryText.value.text.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.onQueryChanged(TextFieldValue(""))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    singleLine = true
                )

                Box {
                    Button(
                        onClick = { queryModesExpanded = true },
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrimaryDarkButton
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = ColorWhite
                        )
                    }

                    DropdownMenu(
                        expanded = queryModesExpanded,
                        onDismissRequest = { queryModesExpanded = false }
                    ) {
                        queryModes.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.displayString) },
                                onClick = {
                                    viewModel.onQueryModeChanged(mode)
                                    queryModesExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = if (mode.isSelected) {
                                        ColorPrimary
                                    } else {
                                        Color.Unspecified
                                    }
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dictionary selector spinner
            DictionaryDropdown(
                dictionaries = dictionaries,
                currentDictionary = (queryResult?.dictionary as? LoadableContent.Completed)?.content,
                onDictionarySelected = { viewModel.onDictionaryChanged(it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Results: loader until words are ready, list when completed
            when (val wordList = queryResult?.wordList) {
                null,
                LoadableContent.UnInitialized,
                LoadableContent.Loading,
                is LoadableContent.Failed -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is LoadableContent.Completed -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(wordList.content) { word ->
                            WordItem(
                                wordView = word,
                                onClick = {
                                    navController.navigate(Screen.DictionaryTranslation.route)
                                    viewModel.onWordSelected(word)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


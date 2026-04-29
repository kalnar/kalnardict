package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.kalnarapps.kalnardict.android.utils.uri.UriAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.compose.ManageableDictionaryItem
import eu.kalnarapps.kalnardict.androidui.theme.ColorPrimary
import eu.kalnarapps.kalnardict.androidui.theme.ColorWhite
import eu.kalnarapps.kalnardict.androidui.theme.GradientBackground
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import org.koin.compose.koinInject


@Composable
fun DictionaryManagerScreen(
    viewModel: DictionaryManagerViewModel = viewModel(factory = DictionaryManagerViewModelFactory()),
    navigateToDictionaryRegistry: (String) -> Unit,
    navigateToPermissionError: () -> Unit,
) {
    val dictionaries by viewModel.getRegisteredDictionaries().observeAsState()

    val uriAdapter: UriAdapter = koinInject()

    val dbBrowserLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        navigateToDictionaryRegistry.invoke(uriAdapter.convertUriToSdcardPath(uri))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackground),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Dictionary Manager",
                color = ColorWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            when (val state = dictionaries) {
                null,
                LoadableContent.UnInitialized,
                LoadableContent.Loading,
                is LoadableContent.Failed -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(top = 20.dp)
                    ) {
                        CircularProgressIndicator(
                            color = ColorWhite,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is LoadableContent.Completed -> {
                    if (state.content.isEmpty()) {
                        Text(
                            text = "No registered dictionaries found. Please register a dictionary from an external database",
                            color = ColorWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 32.dp,
                                    vertical = 12.dp
                                )
                        )
                    }

                    LazyColumn {
                        items(
                            items = state.content,
                            key = { it.dictionaryId }
                        ) { dictionary ->
                            ManageableDictionaryItem(
                                dictionary = dictionary,
                                onUpdate = { viewModel.updateDictionary(it) },
                                onDelete = { dictionary.onDeleteAction?.invoke(it) }
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    if (hasManageExternalStoragePermission()) {
                                        dbBrowserLauncher.launch(
                                            "*/*"
                                        )
                                    } else {
                                        navigateToPermissionError.invoke()
                                    }
                              },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(vertical = 10.dp)
                            ) {
                                Text("IMPORT NEW DICTIONARIES")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun hasManageExternalStoragePermission(): Boolean {
    return (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager())
}

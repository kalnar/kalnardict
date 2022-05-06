package eu.kalnarapps.kalnardict.androidui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MockDbImportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MockDbScreen()
        }
    }

}

@Composable
@Preview
fun MockDbScreen() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MockCard()
        }
    }

}

@Composable
@Preview
fun MockCard() {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Mock DB creator",
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp, bottom = 24.dp),
            style = MaterialTheme.typography.h4
        )

        CardComponent()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardComponent() {
    Card(
        shape = RoundedCornerShape(5),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
    ) {
        Column {

            val databaseName = remember {
                mutableStateOf("")
            }

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 25.dp, bottom = 5.dp),
                text = "Create a mock db table which will be populated with random items."
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = databaseName.value,
                onValueChange = {
                    databaseName.value = it
                },
                placeholder = {
                    Text(text = "choose the new mock database name")
                },
                label = {
                    Text(text = "Database name")
                },
                singleLine = true
            )

            val databases = listOf("db1", "db2", "db3")

            ChipList(
                list = databases,
                currentSelection = databaseName.value,
                onSelect = {
                    databaseName.value = it
                }
            )

            val tableName = remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = tableName.value,
                onValueChange = {
                    tableName.value = it
                },
                placeholder = {
                    Text(text = "choose the new mock table name")
                },
                label = {
                    Text(text = "Table name")
                },
                singleLine = true
            )

            val sourceLanguage = remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = sourceLanguage.value,
                onValueChange = {
                    sourceLanguage.value = it
                },
                placeholder = {
                    Text(text = "choose the source language")
                },
                label = {
                    Text(text = "Source language")
                },
                singleLine = true
            )

            val languages = (1..20).map { "language $it" }

            ChipList(
                list = languages,
                currentSelection = sourceLanguage.value,
                onSelect = {
                    sourceLanguage.value = it
                }
            )

            val destinationLanguage = remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = destinationLanguage.value,
                onValueChange = {
                    destinationLanguage.value = it
                },
                placeholder = {
                    Text(text = "choose the destination language")
                },
                label = {
                    Text(text = "Destination language")
                },
                singleLine = true
            )

            ChipList(
                list = languages,
                currentSelection = destinationLanguage.value,
                onSelect = {
                    destinationLanguage.value = it
                }
            )

            Button(
                enabled = databaseName.value.isNotBlank() &&
                        tableName.value.isNotBlank() &&
                        sourceLanguage.value.isNotBlank() &&
                        destinationLanguage.value.isNotBlank(),
                onClick = { /* ... */ },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                ),
                modifier = Modifier
                    .padding(top = 15.dp, end = 10.dp)
                    .align(Alignment.End)

            ) {
                // Inner content including an icon and a text label
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Add mock table")
            }

            Spacer(Modifier.size(25.dp))

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipList(list: List<String>, currentSelection: String, onSelect: ((String) -> Unit)) {

    LazyRow(
        modifier = Modifier.padding(end = 10.dp)
    ) {
        items(list) {
            val isSelected = currentSelection == it
            Chip(
                border = ChipDefaults.outlinedBorder,
                colors = if (isSelected) {
                    ChipDefaults.outlinedChipColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant
                    )
                } else {
                    ChipDefaults.outlinedChipColors()
                },
                onClick = {
                    onSelect.invoke(it)
                },
                modifier = Modifier.padding(start = 10.dp),
                leadingIcon = {
                    if (isSelected) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Checked",
                            modifier = Modifier
                                .size(ButtonDefaults.IconSize)
                                .padding(start = 4.dp)
                        )
                    }
                }
            ) {
                Text(it)
            }
        }
    }
}
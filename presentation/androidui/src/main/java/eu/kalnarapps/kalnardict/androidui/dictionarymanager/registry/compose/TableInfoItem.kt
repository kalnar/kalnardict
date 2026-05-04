package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegisterDictionaryUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

@Composable
fun TableInfoItem(
    registerDictionaryUi: RegisterDictionaryUi,
    onTableInfoChanged: (ExternalTableUiInfo) -> Unit
) {
    val tableInfo = registerDictionaryUi.tableUiInfo
    val availableLanguages = registerDictionaryUi.availableLanguages

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Title + selection switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tableInfo.originalTableName,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = tableInfo.isSelected,
                    onCheckedChange = {
                        onTableInfoChanged(
                            ExternalTableUiInfo(
                                originalTableName = tableInfo.originalTableName,
                                dictionaryName = tableInfo.dictionaryName,
                                originalLanguageFrom = tableInfo.originalLanguageFrom,
                                originalLanguageTo = tableInfo.originalLanguageTo,
                                languageFromUi = tableInfo.languageFromUi,
                                languageToUi = tableInfo.languageToUi,
                                isSelected = it,
                            )
                        )
                    },
                    modifier = Modifier.testTag("table_register_switch")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name to register
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name to register:",
                    modifier = Modifier.width(140.dp)
                )
                OutlinedTextField(
                    value = TextFieldValue(
                        tableInfo.dictionaryName,
                        TextRange(
                            tableInfo.dictionaryNameCursorIndexStart,
                            tableInfo.dictionaryNameCursorIndexEnd
                        )
                    ),
                    onValueChange = {
                        onTableInfoChanged(
                            ExternalTableUiInfo(
                                originalTableName = tableInfo.originalTableName,
                                dictionaryName = it.text,
                                dictionaryNameCursorIndexStart = it.selection.start,
                                dictionaryNameCursorIndexEnd = it.selection.end,
                                originalLanguageFrom = tableInfo.originalLanguageFrom,
                                originalLanguageTo = tableInfo.originalLanguageTo,
                                languageFromUi = tableInfo.languageFromUi,
                                languageToUi = tableInfo.languageToUi,
                                isSelected = tableInfo.isSelected,
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = { Text("New dictionary name") },
                    enabled = tableInfo.isSelected
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Source language
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Source language:",
                        modifier = Modifier.width(140.dp)
                    )
                    Text(
                        text = tableInfo.originalLanguageFrom,
                        modifier = Modifier.width(140.dp)
                    )
                }
                LanguageDropdown(
                    languages = availableLanguages,
                    selected = tableInfo.languageFromUi,
                    onSelected = {
                        onTableInfoChanged(
                            ExternalTableUiInfo(
                                originalTableName = tableInfo.originalTableName,
                                dictionaryName = tableInfo.dictionaryName,
                                originalLanguageFrom = tableInfo.originalLanguageFrom,
                                originalLanguageTo = tableInfo.originalLanguageTo,
                                languageFromUi = it,
                                languageToUi = tableInfo.languageToUi,
                                isSelected = tableInfo.isSelected,
                            )
                        )
                    },
                    isEnabled = tableInfo.isSelected,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Destination language
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Destination language:",
                        modifier = Modifier.width(140.dp)
                    )
                    Text(
                        text = tableInfo.originalLanguageTo,
                        modifier = Modifier
                            .width(140.dp)
                            .padding(end = 8.dp)
                    )
                }
                LanguageDropdown(
                    languages = availableLanguages,
                    selected = tableInfo.languageToUi,
                    onSelected = {
                        onTableInfoChanged(
                            ExternalTableUiInfo(
                                originalTableName = tableInfo.originalTableName,
                                dictionaryName = tableInfo.dictionaryName,
                                originalLanguageFrom = tableInfo.originalLanguageFrom,
                                originalLanguageTo = tableInfo.originalLanguageTo,
                                languageFromUi = tableInfo.languageFromUi,
                                languageToUi = it,
                                isSelected = tableInfo.isSelected,
                            )
                        )
                    },
                    isEnabled = tableInfo.isSelected,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropdown(
    languages: List<SelectableLanguage.LanguageUi>,
    selected: SelectableLanguage,
    onSelected: (SelectableLanguage) -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = when (selected) {
        is SelectableLanguage.LanguageUi -> "${selected.name} (${selected.code})"
        SelectableLanguage.NotSet -> ""
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (isEnabled) {
                expanded = !expanded
            }
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text("${language.name} (${language.code})") },
                    onClick = {
                        onSelected(language)
                        expanded = false
                    },
                    modifier = Modifier.testTag("language_drop_down")
                )
            }
        }
    }
}

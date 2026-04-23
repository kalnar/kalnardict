package eu.kalnarapps.kalnardict.androidui.dictionaryquery.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryDropdown(
    dictionaries: LoadableContent<List<DictionaryUiModel>>?,
    currentDictionary: DictionaryUiModel?,
    onDictionarySelected: (DictionaryUiModel) -> Unit
) {
    val items = (dictionaries as? LoadableContent.Completed)?.content ?: emptyList()
    var expanded by remember { mutableStateOf(false) }
    val displayText = currentDictionary?.let { "${it.displayString} (${it.description})" } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { dictionary ->
                DropdownMenuItem(
                    text = { Text("${dictionary.displayString} (${dictionary.description})") },
                    onClick = {
                        onDictionarySelected(dictionary)
                        expanded = false
                    }
                )
            }
        }
    }
}


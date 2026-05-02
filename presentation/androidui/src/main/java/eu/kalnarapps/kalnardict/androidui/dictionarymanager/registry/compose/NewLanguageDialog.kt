package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.kalnarapps.kalnardict.androidui.common.compose.SemanticTags
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

@Composable
fun NewLanguageDialog(
    duplicateIdError: Boolean,
    onRegister: (SelectableLanguage.LanguageUi) -> Unit,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add new language") },
        text = {
            Column {
                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it },
                    label = { Text("code:") },
                    isError = duplicateIdError,
                    supportingText = if (duplicateIdError) {
                        { Text("id already used, please use another id", color = Color.Red) }
                    } else null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(SemanticTags.languageDialogCode)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = { descriptionInput = it },
                    label = { Text("description:") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(SemanticTags.languageDialogName)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onRegister(
                        SelectableLanguage.LanguageUi(
                            code = codeInput,
                            name = descriptionInput
                        )
                    )
                }
            ) {
                Text("Register language")
            }
        },
        dismissButton = null
    )
}

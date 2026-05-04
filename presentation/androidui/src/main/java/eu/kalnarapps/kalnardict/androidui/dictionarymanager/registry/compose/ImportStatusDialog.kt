package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.kalnarapps.kalnardict.androidui.common.compose.SemanticTags
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableStatus

private val ColorSuccess = Color(0xFF587547)
private val ColorError = Color(0xFFB04C45)

@Composable
fun ImportStatusDialog(
    importStatus: List<ImportTableStatus>,
    onGoToQueryScreen: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = "Table imports status",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            LazyColumn {
                items(importStatus) { status ->
                    ImportStatusItem(status = status)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = onGoToQueryScreen) {
                Text("Go back to query screen")
            }
        }
    )
}

@Composable
private fun ImportStatusItem(status: ImportTableStatus) {
    when (val progress = status.progress) {
        is DataOperationResult.Success -> {
            val isComplete = progress.data.totalRows <= progress.data.registeredRows
            if (isComplete) {
                SuccessItem(status)
            } else {
                ProcessingItem(
                    status = status,
                    registeredRows = progress.data.registeredRows,
                    totalRows = progress.data.totalRows
                )
            }
        }
        is DataOperationResult.Failure -> FailedItem(status)
    }
}

@Composable
private fun SuccessItem(status: ImportTableStatus) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = ColorSuccess,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 4.dp)
                .testTag(SemanticTags.importTableSuccessIcon)
        )
        Text(
            text = "Table ${status.table.originalTableName} is imported successfully as ${status.table.dictionaryName}.",
            color = ColorSuccess
        )
    }
}

@Composable
private fun ProcessingItem(
    status: ImportTableStatus,
    registeredRows: Int,
    totalRows: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Table ${status.table.originalTableName} is being imported…")
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { if (totalRows > 0) registeredRows.toFloat() / totalRows else 0f },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .testTag(SemanticTags.importTableProgress)
            )
            Text(text = "$registeredRows/$totalRows")
        }
    }
}

@Composable
private fun FailedItem(status: ImportTableStatus) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = ColorError,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = "The import of table ${status.table.originalTableName} failed.",
            color = ColorError
        )
    }
}

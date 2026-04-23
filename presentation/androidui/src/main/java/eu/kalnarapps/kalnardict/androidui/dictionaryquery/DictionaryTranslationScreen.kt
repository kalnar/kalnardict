package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views.SimpleTextTranslationView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.translation.views.WebViewHtmlTranslationView
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

@Composable
fun DictionaryTranslationScreen(
    viewModel: DictionaryQueryViewModel = viewModel(factory = DictionaryQueryViewModelFactory()),
) {
    val translationState by viewModel.getTranslation().observeAsState()
    val queryResult by viewModel.getQueryResult().observeAsState()
    val context = LocalContext.current

    var content by remember { mutableStateOf<String?>(null) }
    var strategy by remember { mutableStateOf<RenderingStrategy?>(null) }

    val completed = translationState as? LoadableContent.Completed

    LaunchedEffect(completed?.content) {
        when (val result = completed?.content) {
            is DataOperationResult.Success -> {
                val event = result.data
                if (!event.hasBeenHandled()) {
                    val dictionary =
                        (queryResult?.dictionary as? LoadableContent.Completed)?.content
                    if (dictionary != null) {
                        content = event.content()
                        strategy = dictionary.renderingStrategy
                    }
                }
            }
            is DataOperationResult.Failure -> {
                Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
            }
            null -> Unit
        }
    }

    val currentContent = content ?: return
    val currentStrategy = strategy ?: return

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            if (currentStrategy.id == "html") {
                key(currentContent) {
                    AndroidView(
                        factory = { ctx ->
                            WebViewHtmlTranslationView(ctx).apply {
                                loadContent(currentContent)
                            }
                        },
                    )
                }
            } else {
                key(currentContent) {
                    AndroidView(
                        factory = { ctx ->
                            SimpleTextTranslationView(ctx).apply {
                                loadContent(currentContent)
                            }
                        },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

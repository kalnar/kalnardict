package eu.kalnarapps.kalnardict.presentation.models.dictionaryquery

import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

data class DictionaryUiModel(
    val id: Int,
    val displayString: String,
    val description: String,
    val renderingStrategy: RenderingStrategy,
    val supportedRenderingStrategies: List<RenderingStrategy>
)

sealed class DictionarySelection {
    data class Current(val uiModel: DictionaryUiModel) : DictionarySelection()
    object NotAvailable : DictionarySelection()
}
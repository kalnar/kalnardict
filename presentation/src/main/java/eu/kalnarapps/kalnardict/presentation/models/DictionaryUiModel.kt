package eu.kalnarapps.kalnardict.presentation.models

import eu.kalnarapps.kalnardict.models.translations.RenderingStrategy

data class DictionaryUiModel(
    val id: Int,
    val displayString: String,
    val description: String,
    val renderingStrategy: RenderingStrategy,
    val supportedRenderingStrategies: List<RenderingStrategy>
)
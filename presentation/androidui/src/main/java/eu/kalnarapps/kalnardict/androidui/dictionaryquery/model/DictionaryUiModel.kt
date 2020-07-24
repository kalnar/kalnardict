package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

data class DictionaryUiModel(
    val id: Int,
    val displayString: String,
    val description: String,
    val renderingStrategy: RenderingStrategy = RenderingStrategy.HTML
)
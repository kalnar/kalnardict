package eu.kalnarapps.kalnardict.presentation.models.translations

import eu.kalnarapps.kalnardict.presentation.models.common.SimpleListItem

data class RenderingStrategy(
    override val id: String,
    override val displayString: String
) : SimpleListItem
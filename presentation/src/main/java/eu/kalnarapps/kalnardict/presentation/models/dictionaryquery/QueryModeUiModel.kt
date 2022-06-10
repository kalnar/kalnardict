package eu.kalnarapps.kalnardict.presentation.models.dictionaryquery

data class QueryModeUiModel(
    override val id: Int,
    override val displayString: String,
    override val isSelected: Boolean = false
) : ListTextItem
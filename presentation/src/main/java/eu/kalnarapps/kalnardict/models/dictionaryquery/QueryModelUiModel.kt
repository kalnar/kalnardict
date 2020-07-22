package eu.kalnarapps.kalnardict.models.dictionaryquery

data class QueryModelUiModel(
    override val id: Int,
    override val displayString: String,
    override val isSelected: Boolean = false
) : ListTextItem
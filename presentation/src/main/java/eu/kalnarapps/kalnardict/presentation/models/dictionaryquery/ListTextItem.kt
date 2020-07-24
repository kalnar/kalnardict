package eu.kalnarapps.kalnardict.presentation.models.dictionaryquery

interface ListTextItem {
    val id: Int
    val displayString: String
    val isSelected: Boolean
        get() = false
}


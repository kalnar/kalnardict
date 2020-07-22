package eu.kalnarapps.kalnardict.models.dictionaryquery

interface ListTextItem {
    val id: Int
    val displayString: String
    val isSelected: Boolean
        get() = false
}


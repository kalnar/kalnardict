package eu.kalnarapps.kalnardict.data.model

enum class DisplayType(val id: String) {
    HTML("html"),
    SIMPLE_TEXT("text");

    companion object {
        fun fromId(id: String): DisplayType? {
            return values().associateBy(DisplayType::id)[id]
        }
    }
}
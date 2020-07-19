package eu.kalnarapps.kalnardict.domain.entities.dictionary


data class DictQuery(
    val queryString: String,
    val dictionary: Dictionary,
    val accentMode: AccentMode = AccentMode.ACCENT_SENSITIVE,
    val queryMode: QueryMode = QueryMode.MATCH_ANYWHERE
)

enum class AccentMode {
    IGNORE_ACCENTS,
    SMART_ACCENTS,
    ACCENT_SENSITIVE
}

enum class QueryMode(val value: Int) {
    MATCH_ANYWHERE(0),
    MATCH_BEGINNING(1),
    MATCH_END(2),
    MATCH_EXACT(3),
    MATCH_FUZZY(4);

    companion object {
        fun fromId(id: Int): QueryMode {
            return values().associateBy(QueryMode::value)[id] ?: MATCH_BEGINNING
        }
    }
}

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

enum class QueryMode {
    MATCH_ANYWHERE,
    MATCH_BEGINNING,
    MATCH_END,
    MATCH_EXACT,
    MATCH_FUZZY;
}

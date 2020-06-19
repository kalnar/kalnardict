package eu.kalnarapps.kalnardict.domain.entities.dictionary


data class DictQuery(
    val queryString: String,
    val dictionary: Dictionary,
    val accentMode: AccentMode = AccentMode.ACCENT_SENSITIVE
)

enum class AccentMode {
    IGNORE_ACCENTS,
    SMART_ACCENTS,
    ACCENT_SENSITIVE
}

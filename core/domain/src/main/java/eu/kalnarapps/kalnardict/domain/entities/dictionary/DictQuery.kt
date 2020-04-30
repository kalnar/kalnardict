package eu.kalnarapps.kalnardict.domain.entities.dictionary


data class DictQuery(
    val queryString: String,
    val language: DictLanguage,
    val accentMode: AccentMode = AccentMode.ACCENT_SENSITIVE
)

enum class AccentMode {
    IGNORE_ACCENTS,
    SMART_ACCENTS,
    ACCENT_SENSITIVE
}

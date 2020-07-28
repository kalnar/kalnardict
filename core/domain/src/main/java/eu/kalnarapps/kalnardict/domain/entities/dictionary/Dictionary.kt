package eu.kalnarapps.kalnardict.domain.entities.dictionary

import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

data class Dictionary(
    val id: Int,
    val languageFrom: DictLanguage,
    val languageTo: DictLanguage,
    val description: String
)

data class DictionaryWithDisplayTypeInfo(
    val dictionary: Dictionary,
    val displayTypeInfo: DisplayTypeInfo
)

data class DisplayTypeInfo(
    val displayType: DictionaryDisplayType,
    val supportedDisplayTypes: List<DictionaryDisplayType>
)

enum class DictionaryDisplayType(val id: String) {
    HTML("html"), TEXT("text");

    companion object {
        fun fromId(id: String): DictionaryDisplayType {
            return values().associateBy(DictionaryDisplayType::id)[id] ?: error(
                "${id.ifBlank { "<blank_id>" }} is not associated to any DictionaryDisplayType"
            )
        }
    }
}

data class DictLanguage(
    val name: String,
    val code: String
)

data class DictTranslation(
    val id: Int,
    val dictionary: Dictionary,
    val word: DictWord,
    val translation: String
)


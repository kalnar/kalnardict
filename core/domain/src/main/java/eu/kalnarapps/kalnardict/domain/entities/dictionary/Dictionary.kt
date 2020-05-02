package eu.kalnarapps.kalnardict.domain.entities.dictionary

import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

data class Dictionary(
    val id: Int,
    val languageFrom: DictLanguage,
    val LanguageTo: DictLanguage,
    val description: String
)

data class DictLanguage(
    val id: Int,
    val name: String,
    val code: String
)

data class DictTranslation(
    val id: Int,
    val dictionary: Dictionary,
    val word: DictWord,
    val translation: String
)


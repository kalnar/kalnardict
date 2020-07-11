package eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.model

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

data class MockDictEntry(
    val word: DictWord,
    val translation: DictTranslation,
    val dictionary: Dictionary
)
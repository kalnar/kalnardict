package eu.kalnarapps.kalnardict.domain.entities.updates

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType

data class DictionaryDisplayTypeUpdate(
    val dictionaryDisplayType: DictionaryDisplayType,
    val dictionary: Dictionary
)
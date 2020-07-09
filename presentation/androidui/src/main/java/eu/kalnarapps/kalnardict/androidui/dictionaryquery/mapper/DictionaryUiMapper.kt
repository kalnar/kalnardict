package eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper

import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionarySelectorItem
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary


fun Dictionary.toDictionarySelectorItem(): DictionarySelectorItem {
    return DictionarySelectorItem(
        this.id,
        "${this.languageFrom.code} -> ${this.languageTo.code}",
        this.description
    )
}


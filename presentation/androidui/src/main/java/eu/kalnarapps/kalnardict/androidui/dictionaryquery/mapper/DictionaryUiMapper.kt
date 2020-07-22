package eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper

import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryUiModel
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary


fun Dictionary.toDictionarySelectorItem(): DictionaryUiModel {
    return DictionaryUiModel(
        this.id,
        "${this.languageFrom.code} -> ${this.languageTo.code}",
        this.description
    )
}


package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.Stubs.Languages.english
import eu.kalnarapps.kalnardict.data.Stubs.Languages.french
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

object Stubs {
    object Languages {
        val french = DictLanguage(
            name = "French",
            code = "fr"
        )
        val english = DictLanguage(
            name = "English",
            code = "en"
        )
    }

    object Dictionaries {
        val frenchEnglishDictionary = Dictionary(
            id = 2,
            languageFrom = french,
            languageTo = english,
            description = "French to English dictionary"
        )
        val newDictionary = Dictionary(
            id = 3,
            languageFrom = french,
            languageTo = english,
            description = "French to English dictionary"
        )
    }
}
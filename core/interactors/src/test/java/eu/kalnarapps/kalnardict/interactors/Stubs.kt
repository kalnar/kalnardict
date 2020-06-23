package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

object Stubs {
    object RegisteringDictionary {
        const val dbUriStub = "dbUri"
        const val dictionaryName = "newDictionary"
        const val originalTableName = "fr_en_dicitonary"
        const val sourceLangCode = "fr"
        const val destinationLangCode = "en"
    }

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
}
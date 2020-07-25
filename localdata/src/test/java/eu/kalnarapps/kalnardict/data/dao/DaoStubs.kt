package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.model.DictionaryDisplayTypeData

object DaoStubs {
    object Dictionaries {
        val frenchToFrenchDicitonaryId = 1
    }
    object DisplayTypes {
        const val HTML_DISPLAY_TYPE = "html"
        val frenchToFrenchDictionaryDisplayType = DictionaryDisplayTypeData(
            id = HTML_DISPLAY_TYPE
        )
    }
}
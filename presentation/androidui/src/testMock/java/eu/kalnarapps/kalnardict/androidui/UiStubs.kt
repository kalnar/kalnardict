package eu.kalnarapps.kalnardict.androidui

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.model.ExternalTableUiInfo

object UiStubs {
    object DictionaryManager {
        const val newDictionaryName = "newDictionary"
        val externalTable = ExternalTableUiInfo(
            dictionaryName = newDictionaryName,
            languageFrom = "ru",
            languageTo = "en",
            dbPath = "mock",
            tableName = "ru_en_dictionary"
        )
    }
}
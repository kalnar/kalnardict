package eu.kalnarapps.kalnardict.androidui.stub

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import java.net.URI

object UiStubs {

    object Uris {
        val validUri = URI("valid")
        val invalidUri = URI("invalid")
    }

    object TableUiInfo {
        const val newDictionaryName = "newDictionary"
        val externalTable1 = ExternalTableUiInfo(
            dictionaryName = newDictionaryName,
            originalLanguageFrom = "ru",
            originalLanguageTo = "en",
            originalTableName = "ru_en_dictionary",
            isSelected = true
        )
        val externalTable2 = ExternalTableUiInfo(
            dictionaryName = "russian dict",
            originalLanguageFrom = "ru",
            originalLanguageTo = "ru",
            originalTableName = "ru_ru_dictionary",
            isSelected = true
        )
    }

    object TableDbInfo {
        val externalTableInfo1 = ExternalDatabaseTable(
            name = "ru_en_dictionary",
            languageFrom = "ru",
            languageTo = "en"
        )
        val externalTableInfo2 = ExternalDatabaseTable(
            name = "ru_ru_dictionary",
            languageFrom = "ru",
            languageTo = "ru"
        )
    }
}
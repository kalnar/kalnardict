package eu.kalnarapps.kalnardict.androidui.stub

import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

object UiStubs {

    object Uris {
        const val validUri = "valid"
        const val invalidUri = "invalid"
    }

    object TableUiInfo {
        const val newDictionaryName = "newDictionary"
        val langUiEn = SelectableLanguage.LanguageUi(name = "English", code = "en")
        val langUiRu = SelectableLanguage.LanguageUi(name = "Russian", code = "ru")
        val externalTable1 = ExternalTableUiInfo(
            dictionaryName = newDictionaryName,
            originalLanguageFrom = "ru",
            languageFromUi = langUiRu,
            originalLanguageTo = "en",
            languageToUi = langUiEn,
            originalTableName = "ru_en_dictionary",
            isSelected = true
        )
        val externalTable2 = ExternalTableUiInfo(
            dictionaryName = "russian dict",
            originalLanguageFrom = "ru",
            originalLanguageTo = "ru",
            languageFromUi = langUiRu,
            languageToUi = langUiRu,
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
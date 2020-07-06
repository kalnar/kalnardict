package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.Stubs.Languages.english
import eu.kalnarapps.kalnardict.data.Stubs.Languages.french
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable

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
        const val nonExistingLanguageId = "N/A"
        val frenchAndEnglish = listOf(french, english)
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

    object MetaInfoOnDb {
        val table1 = ExternalDatabaseTable(
            "fr_en_dicitonary",
            french.code,
            english.code
        )
        val table2 = ExternalDatabaseTable(
            "en_en_dicitonary",
            Languages.english.code,
            Languages.english.code
        )
    }

    object Db {
        val validExternalDatabase = ExternalDatabase.LocalFile("valid")
        val invalidExternalDatabase = ExternalDatabase.LocalFile("invalid")
    }
}
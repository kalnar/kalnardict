package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import java.net.URI

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
        val all = listOf(french, english)
    }

    object MetaInfoOnDb {
        val table1 = ExternalDatabaseTable(
            "fr_en_dicitonary",
            Languages.french.code,
            Languages.english.code
        )
        val table2 = ExternalDatabaseTable(
            "en_en_dicitonary",
            Languages.english.code,
            Languages.english.code
        )
    }

    object Uris {
        val invalidUri = URI("invalid")
        val valid = URI("valid")
    }
}
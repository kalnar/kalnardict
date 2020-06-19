package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import java.net.URI

val sampleQueryNewWord = DictQuery(
    "new",
    dictionary = DictLanguage("english", "en")
)

val sampleExternalDbTable = ExternalDatabaseTable(
    name = "en_dictionary",
    languageFrom = DictLanguage("english", "en"),
    languageTo = DictLanguage("french", "fr")
)

val newWords = listOf<DictEntry>(
    object : DictEntry {
        override fun getId(): Int = 1
        override fun getBaseForm(): String = "new"
        override fun getAlternativeBaseForm(): String = "new"
        override fun getTranslation(): String = "nouveau"
        override fun getDictionaryId(): Int = 1

    }
)

val validExternalResource = ExternalDatabase.LocalFile(
    uri = URI("mockDbPath")
)

val invalidExternalResource = ExternalDatabase.LocalFile(
    uri = URI("mockInvalidDbPath")
)

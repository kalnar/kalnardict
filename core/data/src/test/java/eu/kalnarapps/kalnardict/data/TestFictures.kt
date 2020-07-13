package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable

val sampleQueryNewWord = DictQuery(
    "nouveau",
    dictionary = Dictionary(
        id = 3,
        languageFrom = DictLanguage("French", "fr"),
        languageTo = DictLanguage("English", "en"),
        description = "French to English dictionary"
    )
)

val sampleExternalDbTable = ExternalDatabaseTable(
    name = "en_dictionary",
    languageFrom = DictLanguage("english", "en").code,
    languageTo = DictLanguage("french", "fr").code
)

val newWordsInFrench = listOf<WordDataEntry>(
    MockWordDataEntry(
        id = 1,
        baseForm = "nouveau",
        alternativeBaseForm = "new",
        translation = "new"
    )
//    object : DictEntry {
//        override fun getId(): Int = 1
//        override fun getBaseForm(): String = "new"
//        override fun getAlternativeBaseForm(): String = "new"
//        override fun getTranslation(): String = "nouveau"
//        override fun getDictionaryId(): Int = 2
//
//    }
)

data class MockWordDataEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val translation: String
) : WordDataEntry

val validExternalResource = ExternalDatabase.LocalFile(
    localPath = "mockDbPath"
)

val invalidExternalResource = ExternalDatabase.LocalFile(
    localPath = "mockInvalidDbPath"
)

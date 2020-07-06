package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.DictEntry


val newWordToInsert = Word(
    id = 2,
    baseForm = "szem",
    alternativeBaseForm = "szem",
    translation = "eye",
    dictionaryId = 1
)

val sampleTableInHungarian = Word(
    id = 1,
    baseForm = "asztal",
    alternativeBaseForm = "asztal",
    translation = "table",
    dictionaryId = 1
)

const val DICTIONARY_ID_FIRST = 1
const val DICTIONARY_ID_SECOND = 2

val sampleDictionaryLogEntry = DictionaryLogEntry(
    id = DICTIONARY_ID_FIRST,
    dictionaryName = "hu_en_dictionary",
    languageFrom = "hu",
    languageTo = "en",
    description = "test dictionary",
    version = "0.01"
)

val newSampleDictionaryLogEntry = DictionaryLogEntry(
    id = DICTIONARY_ID_SECOND,
    dictionaryName = "en_hu_dictionary",
    languageFrom = "en",
    languageTo = "hu",
    description = "test dictionary",
    version = "0.01"
)

object SampleEyeDictEntry : DictEntry {
    override val id: Int = 2
    override val baseForm: String = "szem"
    override val alternativeBaseForm: String = "szem"
    override val translation: String = "eye"
}

object SampleBoxDictEntry : DictEntry {
    override val id: Int = 3
    override val baseForm: String = "doboz"
    override val alternativeBaseForm: String = "doboz"
    override val translation: String = "box"
}

val sampleDictEntries = listOf(SampleEyeDictEntry, SampleBoxDictEntry)

object Languages {
    val english = Language("en", "English")
    val french = Language("fr", "French")
    val frenchAndEnglish = listOf(english, french)
}
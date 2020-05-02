package eu.kalnarapps.kalnardict.data.database

import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Word


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

//import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
//val hungarianToEnglishTestDictionary = DictLanguage(
//    id = 1,
//    code = "hu_en_dictionary",
//    name = "hungarian to english dictionary"
//)

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

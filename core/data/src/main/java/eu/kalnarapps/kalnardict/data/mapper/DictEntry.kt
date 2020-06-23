package eu.kalnarapps.kalnardict.data.mapper

//interface DictEntry {
//    fun getId(): Int
//    fun getBaseForm(): String
//    fun getAlternativeBaseForm(): String
//    fun getTranslation(): String
//    fun getDictionaryId(): Int
//}
interface DictEntry {
    val id: Int
    val baseForm: String
    val alternativeBaseForm: String
    val translation: String
}

interface DictionaryLogEntryData {
    val id: Int
    val name: String
    val languageFrom: String
    val languageTo: String
}

interface LanguageLogEntryData {
    val id: String
    val name: String
}

interface NewDictionaryLogEntryData {
    val name: String
    val languageFrom: String
    val languageTo: String
}

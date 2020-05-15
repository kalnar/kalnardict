package eu.kalnarapps.kalnardict.data

interface DictEntry {
    fun getId(): Int
    fun getBaseForm(): String
    fun getAlternativeBaseForm(): String
    fun getTranslation(): String
    fun getDictionaryId(): Int
}

interface DictionaryLogEntryData {
    val id: Int
    val name: String
    val languageFrom: String
    val languageTo: String
}

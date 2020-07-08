package eu.kalnarapps.kalnardict.data.mapper

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

data class LanguageLocalDataDto(
    override val id: String,
    override val name: String
) : LanguageLogEntryData

interface NewDictionaryLogEntryData {
    val name: String
    val languageFrom: String
    val languageTo: String
}

package eu.kalnarapps.kalnardict.data.mapper

interface WordDataEntry {
    val id: Int
    val baseForm: String
    val alternativeBaseForm: String
    val dictionaryId: Int
}

interface TranslationDataEntry {
    val id: Int
    val dictionaryId: Int
    val translation: String
}

interface TranslatedWordDataEntry : WordDataEntry, TranslationDataEntry

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

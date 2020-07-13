package eu.kalnarapps.kalnardict.data.mapper


data class DictionaryInfo(
    override val id: Int,
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : DictionaryLogEntryData

data class LanguageEntryToData(
    override val id: String,
    override val name: String
) : LanguageLogEntryData

data class WordEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int
) : WordDataEntry

data class TranslatedWordEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : TranslatedWordDataEntry


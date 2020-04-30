package eu.kalnarapps.kalnardict.data

interface DictEntry {
    fun getId(): Int
    fun getBaseForm(): String
    fun getAlternativeBaseForm(): String
    fun getTranslation(): String
    fun getDictionaryId(): Int
}
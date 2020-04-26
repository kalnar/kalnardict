package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation

class Repository(
    private val dictDao: DictDao
) {

    suspend fun insertDictEntry(dictTranslation: DictTranslation) {
        dictDao.insertDictEntry(
            dictTranslation.toDictEntry()
        )
    }

}

private fun DictTranslation.toDictEntry(): DictEntry {
    return object : DictEntry {
        override fun getId(): Int = id
        override fun getBaseForm(): String = word.baseForm
        override fun getAlternativeBaseForm(): String = word.alternativeForm
        override fun getTranslation(): String = translation
        override fun getDictionaryId(): Int = dictionary.id
    }
}

interface DictDao {
    suspend fun insertDictEntry(dictEntry: DictEntry)
}

interface DictEntry {
    fun getId(): Int
    fun getBaseForm(): String
    fun getAlternativeBaseForm(): String
    fun getTranslation(): String
    fun getDictionaryId(): Int
}

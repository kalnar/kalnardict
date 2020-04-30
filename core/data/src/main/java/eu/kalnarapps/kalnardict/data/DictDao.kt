package eu.kalnarapps.kalnardict.data

interface DictDao {
    suspend fun insertDictEntry(dictEntry: DictEntry)
    suspend fun insertDictEntries(dictEntries: List<DictEntry>)
    suspend fun getDictEntryByQuery(query: String): List<DictEntry>
}
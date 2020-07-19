package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutor
import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutorProvider
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode

class MockQueryExecutorProvider(
    private val mockDb: ArrayList<TranslatedWordDataEntry> = ArrayList<TranslatedWordDataEntry>()
) : QueryExecutorProvider {
    override fun provideQueryExecutor(matchType: QueryMode, accentMode: AccentMode): QueryExecutor {
        return when (matchType) {
            QueryMode.MATCH_ANYWHERE -> MockQueryExecutor { wordData, queryString ->
                wordData.baseForm.contains(queryString)
            }
            QueryMode.MATCH_BEGINNING -> MockQueryExecutor { wordData, queryString ->
                wordData.baseForm.startsWith(queryString)
            }
            QueryMode.MATCH_END -> MockQueryExecutor { wordData, queryString ->
                wordData.baseForm.endsWith(queryString)
            }
            QueryMode.MATCH_EXACT -> MockQueryExecutor { wordData, queryString ->
                wordData.baseForm == queryString
            }
            QueryMode.MATCH_FUZZY -> MockQueryExecutor { wordData, queryString ->
                wordData.baseForm.matches(
                    (queryString.replace(".".toRegex()) {
                        ".*${it.value}"
                    } + ".*").toRegex()
                )
            }
        }
    }

    inner class MockQueryExecutor(
        private val filterCondition: (TranslatedWordDataEntry, String) -> Boolean
    ) : QueryExecutor {
        override suspend fun query(queryString: String, dictionaryId: Int): List<WordDataEntry> {
            return mockDb.filter {
                it.dictionaryId == dictionaryId && filterCondition(
                    it,
                    queryString
                )
            }
        }

    }
}


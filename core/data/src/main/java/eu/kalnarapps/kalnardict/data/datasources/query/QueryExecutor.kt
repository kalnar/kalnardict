package eu.kalnarapps.kalnardict.data.datasources.query

import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode

interface QueryExecutor {
    suspend fun query(queryString: String, dictionaryId: Int): List<WordDataEntry>
}

interface QueryExecutorProvider {
    fun provideQueryExecutor(matchType: QueryMode, accentMode: AccentMode): QueryExecutor
}
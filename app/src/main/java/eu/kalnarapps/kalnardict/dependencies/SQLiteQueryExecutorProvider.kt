package eu.kalnarapps.kalnardict.dependencies

import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutor
import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutorProvider
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import org.koin.core.KoinComponent
import org.koin.core.qualifier.StringQualifier

class SQLiteQueryExecutorProvider : QueryExecutorProvider, KoinComponent {
    override fun provideQueryExecutor(matchType: QueryMode, accentMode: AccentMode): QueryExecutor {
        return when (matchType) {
            QueryMode.MATCH_ANYWHERE ->
                getKoin().get(StringQualifier(QueryMode.MATCH_ANYWHERE.name))
            QueryMode.MATCH_BEGINNING ->
                getKoin().get(StringQualifier(QueryMode.MATCH_BEGINNING.name))
            QueryMode.MATCH_END ->
                getKoin().get(StringQualifier(QueryMode.MATCH_END.name))
            QueryMode.MATCH_EXACT ->
                getKoin().get(StringQualifier(QueryMode.MATCH_EXACT.name))
            QueryMode.MATCH_FUZZY ->
                getKoin().get(StringQualifier(QueryMode.MATCH_FUZZY.name))
        }
    }
}
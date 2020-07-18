package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.dao.query.RoomQueryExecutor
import eu.kalnarapps.kalnardict.data.dao.query.formatter.ExactMatchQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.FuzzyQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchAnyWhereFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchEndQueryFormatter
import eu.kalnarapps.kalnardict.data.dao.query.formatter.MatchStartQueryFormatter
import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutorProvider
import eu.kalnarapps.kalnardict.dependencies.SQLiteQueryExecutorProvider
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module


val queryExecutorModule = module {

    single { MatchAnyWhereFormatter() }
    single { MatchStartQueryFormatter() }
    single { MatchEndQueryFormatter() }
    single { FuzzyQueryFormatter() }
    single { ExactMatchQueryFormatter() }

    single(StringQualifier(QueryMode.MATCH_ANYWHERE.name)) {
        RoomQueryExecutor(
            wordDao = get(),
            queryFormatter = get<MatchAnyWhereFormatter>(),
            wordInfoMapper = get()
        )
    }
    single(StringQualifier(QueryMode.MATCH_BEGINNING.name)) {
        RoomQueryExecutor(
            wordDao = get(),
            queryFormatter = get<MatchStartQueryFormatter>(),
            wordInfoMapper = get()
        )
    }
    single(StringQualifier(QueryMode.MATCH_END.name)) {
        RoomQueryExecutor(
            wordDao = get(),
            queryFormatter = get<MatchEndQueryFormatter>(),
            wordInfoMapper = get()
        )
    }
    single(StringQualifier(QueryMode.MATCH_FUZZY.name)) {
        RoomQueryExecutor(
            wordDao = get(),
            queryFormatter = get<FuzzyQueryFormatter>(),
            wordInfoMapper = get()
        )
    }
    single(StringQualifier(QueryMode.MATCH_EXACT.name)) {
        RoomQueryExecutor(
            wordDao = get(),
            queryFormatter = get<ExactMatchQueryFormatter>(),
            wordInfoMapper = get()
        )
    }

    single { SQLiteQueryExecutorProvider() as QueryExecutorProvider }

}
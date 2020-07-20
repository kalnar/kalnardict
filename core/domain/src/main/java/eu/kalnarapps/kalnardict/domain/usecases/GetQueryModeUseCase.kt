package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.flow.Flow

interface GetQueryModeUseCase {
    operator fun invoke(): Flow<QueryMode>
}
package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModeUseCase
import kotlinx.coroutines.flow.Flow

class GetQueryMode(
    private val queryModeConfigurationRepository: QueryModeConfigurationRepository
) : GetQueryModeUseCase {
    override fun invoke(): Flow<QueryMode> {
        return queryModeConfigurationRepository.getCurrentQueryMode()
    }
}
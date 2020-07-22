package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase

class UpdateQueryMode(
    private val queryModeConfigurationRepository: QueryModeConfigurationRepository
) : UpdateQueryModeUseCase {
    override suspend fun invoke(queryModeId: Int) {
        return queryModeConfigurationRepository.updateCurrentQueryMode(
            QueryMode.fromId(queryModeId)
        )
    }
}
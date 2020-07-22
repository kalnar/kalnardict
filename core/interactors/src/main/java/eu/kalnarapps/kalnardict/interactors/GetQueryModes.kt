package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModesUseCase

class GetQueryModes() : GetQueryModesUseCase {
    override fun invoke(): List<QueryMode> {
        return QueryMode.values().toList()
    }
}
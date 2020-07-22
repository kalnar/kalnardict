package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode

interface GetQueryModesUseCase {
    operator fun invoke(): List<QueryMode>
}
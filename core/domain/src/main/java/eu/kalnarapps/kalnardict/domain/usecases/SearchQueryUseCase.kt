package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.words.DictWord


interface SearchQueryUseCase {

    suspend fun invokeWith(
        query: String,
        queryModeId: Int
    ): List<DictWord>

}
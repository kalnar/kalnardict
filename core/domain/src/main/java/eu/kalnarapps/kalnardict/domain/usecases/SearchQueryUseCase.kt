package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord


interface SearchQueryUseCase {

    suspend fun invokeWith(query: String): List<DictWord>

}
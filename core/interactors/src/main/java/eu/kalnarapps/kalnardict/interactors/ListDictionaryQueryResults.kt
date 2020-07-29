package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase

class ListDictionaryQueryResults(
    private val dictionaryRepository: DictionaryRepository
) : SearchQueryUseCase {
    override suspend fun invokeWith(query: DictQuery): List<DictWord> {
        return dictionaryRepository.getEntriesByQuery(query)
    }
}

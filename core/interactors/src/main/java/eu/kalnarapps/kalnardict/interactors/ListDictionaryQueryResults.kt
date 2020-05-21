package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase

class ListDictionaryQueryResults(
    private val dictionaryRepository: DictionaryRepository,
    private val configurationRepository: ConfigurationRepository
) : SearchQueryUseCase {
    override suspend fun invokeWith(query: String): List<DictWord> {
        return dictionaryRepository.getEntriesByQuery(
            DictQuery(
                queryString = query,
                language = configurationRepository.getCurrentLanguage(),
                accentMode = configurationRepository.getCurrentAccentMode()
            )
        )
    }
}
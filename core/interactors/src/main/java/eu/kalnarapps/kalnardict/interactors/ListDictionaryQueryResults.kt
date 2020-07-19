package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase

// TODO: test
class ListDictionaryQueryResults(
    private val dictionaryRepository: DictionaryRepository,
    private val configurationRepository: ConfigurationRepository,
    private val queryModeConfigurationRepository: QueryModeConfigurationRepository
) : SearchQueryUseCase {
    override suspend fun invokeWith(query: String): List<DictWord> {
        return configurationRepository.getCurrentDictionary().let {
            when (it) {
                is CurrentDictionary.SetDictionary ->
                    dictionaryRepository.getEntriesByQuery(
                        DictQuery(
                            queryString = query,
                            dictionary = it.dictionary,
                            accentMode = configurationRepository.getCurrentAccentMode(),
                            queryMode = queryModeConfigurationRepository.getCurrentQueryMode()
                        )
                    )
                CurrentDictionary.DictionaryNotSet -> emptyList()
            }
        }
    }
}
package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.datasources.LanguageDataSource
import eu.kalnarapps.kalnardict.data.datasources.configuration.ConfigurationDataSource
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

class AppConfigRepository(
    private val configurationDataSource: ConfigurationDataSource,
    private val dictionaryDataSource: DictionaryDataSource,
    private val languageDataSource: LanguageDataSource
) : ConfigurationRepository {
    override suspend fun getCurrentDictionary(): CurrentDictionary {
        return when (
            val fetchDictionary = dictionaryDataSource.getDictionaryById(
                configurationDataSource.getLastDictionaryId()
            )) {
            is DataOperationResult.Success -> {
                getCurrentDictionaryFromData(fetchDictionary.data)
            }
            is DataOperationResult.Failure -> {
                CurrentDictionary.DictionaryNotSet
            }
        }
    }

    private suspend fun getCurrentDictionaryFromData(data: DictionaryLogEntryData): CurrentDictionary {
        return when (val dictionaryFetch = getDictionaryFromData(data)) {
            is DataOperationResult.Success -> CurrentDictionary.SetDictionary(
                dictionaryFetch.data
            )
            is DataOperationResult.Failure -> {
                CurrentDictionary.DictionaryNotSet
            }
        }
    }

    private suspend fun getDictionaryFromData(
        data: DictionaryLogEntryData
    ): DataOperationResult<Dictionary> {
        val sourceLanguageFetch = languageDataSource.getLanguageById(data.languageFrom)
        val destinationLanguageFetch = languageDataSource.getLanguageById(data.languageTo)
        return if (sourceLanguageFetch is DataOperationResult.Success
            && destinationLanguageFetch is DataOperationResult.Success
        ) {
            DataOperationResult.Success(
                Dictionary(
                    id = data.id,
                    languageFrom = sourceLanguageFetch.data.toDictLanguage(),
                    languageTo = destinationLanguageFetch.data.toDictLanguage(),
                    description = data.name
                )
            )
        } else {
            DataOperationResult.firstFailure(sourceLanguageFetch, destinationLanguageFetch)
        }
    }

    override suspend fun getCurrentAccentMode(): AccentMode {
        return AccentMode.ACCENT_SENSITIVE
    }

    override suspend fun updateCurrentDictionary(dictionary: Dictionary) {
        configurationDataSource.updateLastDictionary(dictionaryId = dictionary.id)
    }
}
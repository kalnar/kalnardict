package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

interface ConfigurationRepository {
    suspend fun getCurrentLanguage(): DictLanguage
    suspend fun getCurrentAccentMode(): AccentMode
}
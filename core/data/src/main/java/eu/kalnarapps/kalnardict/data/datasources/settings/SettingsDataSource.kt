package eu.kalnarapps.kalnardict.data.datasources.settings

import eu.kalnarapps.kalnardict.data.model.settings.SettingsKey

interface SettingsDataSource {
    suspend fun getSettingsItem(key: SettingsKey): String
}
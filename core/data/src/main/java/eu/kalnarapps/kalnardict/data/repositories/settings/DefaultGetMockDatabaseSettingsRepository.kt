package eu.kalnarapps.kalnardict.data.repositories.settings

import eu.kalnarapps.kalnardict.data.GetMockDatabaseSettingsRepository
import eu.kalnarapps.kalnardict.data.datasources.settings.SettingsDataSource
import eu.kalnarapps.kalnardict.data.model.settings.SettingsKey

class DefaultGetMockDatabaseSettingsRepository(
    private val settingsDataSource: SettingsDataSource
) : GetMockDatabaseSettingsRepository {
    override suspend fun getMockDatabaseDirectoryPath(): String {
        return settingsDataSource.getSettingsItem(SettingsKey.MOCK_DB_PATH)
    }
}
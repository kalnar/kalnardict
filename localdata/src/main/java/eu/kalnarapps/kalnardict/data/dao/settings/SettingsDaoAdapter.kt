package eu.kalnarapps.kalnardict.data.dao.settings

import android.content.Context
import eu.kalnarapps.kalnardict.common.utils.getStorageRootPath
import eu.kalnarapps.kalnardict.data.datasources.settings.SettingsDataSource
import eu.kalnarapps.kalnardict.data.model.settings.SettingsKey

class SettingsDaoAdapter(
    private val applicationContext: Context
) : SettingsDataSource {
    override suspend fun getSettingsItem(key: SettingsKey): String {
        return when (key) {
            SettingsKey.MOCK_DB_PATH -> applicationContext.getStorageRootPath() + "/Download/"
        }
    }
}


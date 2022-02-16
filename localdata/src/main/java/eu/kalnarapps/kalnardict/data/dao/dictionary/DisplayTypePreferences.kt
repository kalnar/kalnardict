package eu.kalnarapps.kalnardict.data.dao.dictionary

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.preference.PreferenceManager
import com.tfcporciuncula.flow.FlowSharedPreferences
import eu.kalnarapps.kalnardict.data.model.DisplayType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


class DisplayTypePreferences(
    context: Context
) : DisplayTypePreferencesDao {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val flowPreferences = FlowSharedPreferences(sharedPreferences)

    override fun getDisplayTypeForDictionary(dictionaryId: Int): String {
        return flowPreferences.getString(
            key = getPreferenceKeyForDictionary(dictionaryId),
            defaultValue = DisplayType.HTML.id
        ).get()
    }

    override fun getDisplayTypeForDictionaryFlow(dictionaryId: Int): Flow<String> {
        return flowPreferences.getString(
            key = getPreferenceKeyForDictionary(dictionaryId),
            defaultValue = DisplayType.HTML.id
        ).asFlow()
    }

    override suspend fun setDisplayTypeForDictionary(dictionaryId: Int, type: String) {
        sharedPreferences.edit().putString(
            getPreferenceKeyForDictionary(dictionaryId),
            type
        ).apply()
    }

    private fun getPreferenceKeyForDictionary(dictionaryId: Int): String {
        return PreferenceKeys.DISPLAY_TYPE + dictionaryId
    }

    @VisibleForTesting
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

object PreferenceKeys {
    const val DISPLAY_TYPE = "dictionary_display_type_"
}
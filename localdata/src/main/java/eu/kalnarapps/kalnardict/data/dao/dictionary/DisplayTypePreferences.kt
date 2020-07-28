package eu.kalnarapps.kalnardict.data.dao.dictionary

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.preference.PreferenceManager
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.data.model.DisplayType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class DisplayTypePreferences(
    context: Context,
    private val dispatcherProvider: DispatcherProvider
) : DisplayTypePreferencesDao {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getDisplayTypeForDictionary(dictionaryId: Int): Flow<String> {
        return sharedPreferences.observeKey(
            getPreferenceKeyForDictionary(dictionaryId),
            default = DisplayType.HTML.id,
            dispatcher = dispatcherProvider.io()
        )
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
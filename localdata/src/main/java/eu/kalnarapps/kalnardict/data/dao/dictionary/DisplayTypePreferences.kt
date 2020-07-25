package eu.kalnarapps.kalnardict.data.dao.dictionary

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.preference.PreferenceManager
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@ExperimentalCoroutinesApi
class DisplayTypePreferences(
    context: Context,
    private val dispatcherProvider: DispatcherProvider
) : DisplayTypePreferencesDao {

    private var map = HashMap<String, String>()
    private val stringPreferenceChannel = Channel<MutableMap<String, String>>()

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getDisplayTypeForDictionary(dictionaryId: Int): Flow<String> {
        return merge(
            stringPreferenceChannel.consumeAsFlow()
                .filter {
                    getPreferenceKeyForDictionary(dictionaryId) in it
                }.map {
                    it[getPreferenceKeyForDictionary(dictionaryId)]!!
                },
            sharedPreferences.observeKey(
                getPreferenceKeyForDictionary(dictionaryId),
                default = "",
                dispatcher = dispatcherProvider.io()
            )
        ).distinctUntilChanged()
    }

    override suspend fun setDisplayTypeForDictionary(dictionaryId: Int, type: String) {
        sharedPreferences.edit().putString(
            getPreferenceKeyForDictionary(dictionaryId),
            type
        ).apply()
        stringPreferenceChannel.send(
            map.apply {
                put(getPreferenceKeyForDictionary(dictionaryId), type)
            }
        )
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
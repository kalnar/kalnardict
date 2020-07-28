package eu.kalnarapps.kalnardict.android.utils.strings

import android.content.Context
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResources

class DictionaryQueryStringResolver(
    private val context: Context
) : StringResolver<StringResources.DictionaryQueryString> {
    override fun lookUpString(resource: StringResources.DictionaryQueryString): String {
        return when (resource) {
            StringResources.DictionaryQueryString.QUERY_MODE_MATCH_ANYWHERE -> {
                context.getString(R.string.query_mode_match_anywhere)
            }
            StringResources.DictionaryQueryString.QUERY_MODE_MATCH_BEGINNING -> {
                context.getString(R.string.query_mode_match_beginning)
            }
            StringResources.DictionaryQueryString.QUERY_MODE_MATCH_END -> {
                context.getString(R.string.query_mode_match_end)
            }
            StringResources.DictionaryQueryString.QUERY_MODE_MATCH_EXACT -> {
                context.getString(R.string.query_mode_match_exact)
            }
            StringResources.DictionaryQueryString.QUERY_MODE_MATCH_FUZZY -> {
                context.getString(R.string.query_mode_match_fuzzy)
            }
        }
    }
}
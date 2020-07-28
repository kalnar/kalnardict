package eu.kalnarapps.kalnardict.android.utils.strings

import android.content.Context
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResources

class DictionaryRenderingStrategyStringResolver(
    private val context: Context
) : StringResolver<StringResources.DictionaryRenderingStrategies> {
    override fun lookUpString(resource: StringResources.DictionaryRenderingStrategies): String {
        return when (resource) {
            StringResources.DictionaryRenderingStrategies.HTML -> context.getString(
                R.string.dictionary_rendering_html
            )
            StringResources.DictionaryRenderingStrategies.TEXT -> context.getString(
                R.string.dictionary_rendering_text
            )
        }
    }
}
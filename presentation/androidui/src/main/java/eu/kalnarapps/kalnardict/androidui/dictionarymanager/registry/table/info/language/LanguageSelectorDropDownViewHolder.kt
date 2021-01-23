package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorGone
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.common.extentions.exhaustive

class LanguageSelectorDropDownViewHolder(
    inflater: LayoutInflater, parent: ViewGroup?
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.query_language_selector_item,
        parent,
        false
    )
) {
    private val displayTitle: TextView = itemView.findViewById(R.id.selector_title)
    private val descriptionView: TextView = itemView.findViewById(R.id.dictionary_description_view)

    fun bind(queryLanguageView: SelectableLanguage) {
        descriptionView.visibleXorGone(queryLanguageView is SelectableLanguage.LanguageUi)
        when (queryLanguageView) {
            is SelectableLanguage.LanguageUi -> {
                displayTitle.text = queryLanguageView.name
                descriptionView.text = queryLanguageView.code
            }
            SelectableLanguage.NotSet -> {
                displayTitle.text = displayTitle.context.getString(
                    R.string.add_new_language_text
                )
            }
        }.exhaustive
    }
}
package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.common.extentions.exhaustive

class LanguageListItemViewHolder(
    inflater: LayoutInflater, parent: ViewGroup?
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_registry_table_info_language_item_view,
        parent,
        false
    )
) {
    private val displayTitle: TextView = itemView.findViewById(R.id.table_info_language_item)

    fun bind(languageItemUiModel: SelectableLanguage) {
        when (languageItemUiModel) {
            is SelectableLanguage.LanguageUi -> {
                displayTitle.text =
                    displayTitle.context.getString(
                        R.string.selected_language_text,
                        languageItemUiModel.name, languageItemUiModel.code
                    )
            }
            SelectableLanguage.NotSet,
            SelectableLanguage.AddNewLanguage -> {
                displayTitle.text =
                    displayTitle.context.getString(R.string.no_languages_available)
            }
        }.exhaustive
    }
}
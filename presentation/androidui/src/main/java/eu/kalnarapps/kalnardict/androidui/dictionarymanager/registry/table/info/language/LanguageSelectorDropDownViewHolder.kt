package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorGone
import eu.kalnarapps.kalnardict.androidui.common.viewextensions.visibleXorInvisible
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
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
    private val selectableLanguageGroup: Group =
        itemView.findViewById(R.id.selectable_language_group)
    private val displayTitle: TextView = itemView.findViewById(R.id.selector_title)
    private val descriptionView: TextView = itemView.findViewById(R.id.dictionary_description_view)
    private val addNewLanguageButton: TextView = itemView.findViewById(R.id.add_new_language)


    fun bind(queryLanguageView: SelectableLanguage) {
        selectableLanguageGroup.visibleXorInvisible(
            queryLanguageView is SelectableLanguage.LanguageUi
        )
        addNewLanguageButton.visibleXorGone(
            queryLanguageView is SelectableLanguage.AddNewLanguage
        )
        when (queryLanguageView) {
            is SelectableLanguage.LanguageUi -> {
                displayTitle.text = queryLanguageView.name
                descriptionView.text = queryLanguageView.code
            }
            SelectableLanguage.NotSet,
            SelectableLanguage.AddNewLanguage -> Unit
        }.exhaustive
    }
}
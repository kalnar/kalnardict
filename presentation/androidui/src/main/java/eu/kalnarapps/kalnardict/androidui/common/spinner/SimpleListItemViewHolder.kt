package eu.kalnarapps.kalnardict.androidui.common.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.presentation.models.common.SimpleListItem
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.ListTextItem

class SimpleListItemViewHolder(
    inflater: LayoutInflater, parent: ViewGroup?
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.simple_list_item_view,
        parent,
        false
    )
) {
    private val displayTitle: TextView = itemView.findViewById(R.id.simple_list_item)

    fun bind(listTextItem: SimpleListItem) {
        displayTitle.text = listTextItem.displayString
    }
}
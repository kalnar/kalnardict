package eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionarySelectorItem

class DictionarySelectionViewHolder(
    inflater: LayoutInflater, parent: ViewGroup?
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.query_language_selection_item,
        parent,
        false
    )
) {
    private val displayTitle: TextView = itemView.findViewById(R.id.query_language_title)

    fun bind(queryLanguageView: DictionarySelectorItem) {
        displayTitle.text = "${queryLanguageView.displayString} (${queryLanguageView.description})"
    }
}
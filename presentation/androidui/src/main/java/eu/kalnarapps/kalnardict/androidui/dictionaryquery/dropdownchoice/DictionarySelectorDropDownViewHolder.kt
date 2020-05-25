package eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionarySelectorItem

class DictionarySelectorDropDownViewHolder(
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


    fun bind(queryLanguageView: DictionarySelectorItem) {
        displayTitle.text = queryLanguageView.displayString
        descriptionView.text = queryLanguageView.description
    }
}
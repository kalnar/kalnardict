package eu.kalnarapps.kalnardict.androidui.dictionaryquery.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.WordView

class QueryResultItemViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_query_word_item_view,
        parent,
        false
    )
) {
    private val baseFormView: TextView = itemView.findViewById(R.id.word_item_view)

    fun bind(queryResultItem: WordView) {
        baseFormView.text = queryResultItem.baseForm
    }
}
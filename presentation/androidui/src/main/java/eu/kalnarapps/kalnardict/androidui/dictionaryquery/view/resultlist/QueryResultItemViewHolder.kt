package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners.OnWordClickedListener

class QueryResultItemViewHolder(
    inflater: LayoutInflater, parent: ViewGroup,
    private val onWordClickedListener: OnWordClickedListener
) : WordViewHolder(
    inflater.inflate(
        R.layout.dictionary_query_word_item_view,
        parent,
        false
    )
) {
    private val baseFormView: TextView = itemView.findViewById(R.id.word_item_view)
    private val container: MaterialCardView =
        itemView.findViewById(R.id.query_result_item_container)

    override fun bind(wordView: WordView) {
        baseFormView.text = wordView.baseForm
        container.setOnClickListener {
            onWordClickedListener.onWordClicked(wordView)
        }
    }
}
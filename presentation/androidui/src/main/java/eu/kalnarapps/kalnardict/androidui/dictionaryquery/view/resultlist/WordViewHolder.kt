package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView

abstract class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(wordView: WordView)
}
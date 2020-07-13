package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners.OnWordClickedListener

class QueryResultListAdapter(
    private val onWordSelectedListener: OnWordClickedListener
) : RecyclerView.Adapter<WordViewHolder>() {
    private var list: List<WordView> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return QueryResultItemViewHolder(
            inflater,
            parent,
            onWordClickedListener = onWordSelectedListener
        )
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
    fun updateWords(it: List<WordView>) {
        list = it
        notifyDataSetChanged()
    }

}


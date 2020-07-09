package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView

class QueryResultListAdapter : RecyclerView.Adapter<QueryResultItemViewHolder>() {
    private var list: List<WordView> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QueryResultItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return QueryResultItemViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: QueryResultItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
    fun updateWords(it: List<WordView>) {
        list = it
        notifyDataSetChanged()
    }

}


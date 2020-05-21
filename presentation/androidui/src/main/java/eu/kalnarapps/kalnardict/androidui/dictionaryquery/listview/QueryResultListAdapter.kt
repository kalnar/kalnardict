package eu.kalnarapps.kalnardict.androidui.dictionaryquery.listview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.WordView

class QueryResultListAdapter(
    private val list: List<WordView>
) : RecyclerView.Adapter<QueryResultItemViewHolder>() {

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

}


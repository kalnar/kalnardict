package eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.ManageableDictionaryView

class ManageableDictionaryListAdapter(
    private val list: List<ManageableDictionaryView>
) : RecyclerView.Adapter<ManageableDictionaryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageableDictionaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ManageableDictionaryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ManageableDictionaryViewHolder, position: Int) {
        val movie: ManageableDictionaryView = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

}
package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.resultlist.listeners.OnWordClickedListener

class QueryResultListAdapter(
    private val onWordSelectedListener: OnWordClickedListener
) : RecyclerView.Adapter<WordViewHolder>() {

    private val diffCallBack =
        object : DiffUtil.ItemCallback<WordView>() {
            override fun areItemsTheSame(
                oldItem: WordView,
                newItem: WordView
            ): Boolean {
                return oldItem.id ==
                        newItem.id
            }

            override fun areContentsTheSame(
                oldItem: WordView,
                newItem: WordView
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val differ: AsyncListDiffer<WordView> = AsyncListDiffer(
        this,
        diffCallBack
    )

    init {
        differ.submitList(emptyList())
    }

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
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
    fun updateWords(it: List<WordView>) {
        differ.submitList(it)
    }

}


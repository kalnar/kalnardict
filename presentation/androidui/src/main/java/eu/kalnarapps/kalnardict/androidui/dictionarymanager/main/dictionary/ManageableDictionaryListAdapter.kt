package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.ManageableDictionaryView

class ManageableDictionaryListAdapter(
    private val onDictionaryClickListener: OnDictionaryClickListener,
    private val onNewButtonAction: OnNewButtonAction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<ManageableDictionaryView> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DictionaryListViewType.MANAGEABLE_DICTIONARY_VIEW.id -> ManageableDictionaryViewHolder(
                inflater,
                parent,
                onDictionaryClickListener
            )
            else -> NewButtonViewHolder(inflater, parent)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size == position) {
            DictionaryListViewType.NEW_BUTTON_VIEW.id
        } else {
            DictionaryListViewType.MANAGEABLE_DICTIONARY_VIEW.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < list.size) {
            val manageableDictionaryViewHolder = holder as ManageableDictionaryViewHolder
            manageableDictionaryViewHolder.bind(list[position])
        } else {
            val newButtonViewHolder = holder as NewButtonViewHolder
            newButtonViewHolder.bind(onNewButtonAction)
        }
    }

    override fun getItemCount(): Int = list.size + 1

    fun updateList(content: List<ManageableDictionaryView>) {
        list = content
        notifyDataSetChanged()
    }

}

enum class DictionaryListViewType(val id: Int) {
    MANAGEABLE_DICTIONARY_VIEW(0),
    NEW_BUTTON_VIEW(1)
}


interface OnDictionaryClickListener {
    fun onClick(dictionaryView: ManageableDictionaryView)
}

interface OnNewButtonAction {
    fun invoke()
}
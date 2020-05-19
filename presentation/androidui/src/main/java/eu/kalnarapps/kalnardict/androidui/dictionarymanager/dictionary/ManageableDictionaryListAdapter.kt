package eu.kalnarapps.kalnardict.androidui.dictionarymanager.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.ManageableDictionaryView

class ManageableDictionaryListAdapter(
    private val list: List<ManageableDictionaryView>,
    private val onDictionaryClickListener: OnDictionaryClickListener,
    private val onNewButtonAction: OnNewButtonAction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
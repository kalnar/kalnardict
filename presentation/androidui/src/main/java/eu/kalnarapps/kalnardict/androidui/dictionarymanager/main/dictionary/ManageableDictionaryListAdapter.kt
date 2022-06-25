package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView

class ManageableDictionaryListAdapter(
    private val onDictionaryUpdateListener: OnDictionaryUpdateListener,
    private val onNewButtonAction: OnNewButtonAction
) : ListAdapter<ManageableDictionaryView, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DictionaryListViewType.MANAGEABLE_DICTIONARY_VIEW.id -> ManageableDictionaryViewHolder(
                inflater,
                parent,
                onDictionaryUpdateListener
            )
            else -> NewButtonViewHolder(inflater, parent)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.size == position) {
            DictionaryListViewType.NEW_BUTTON_VIEW.id
        } else {
            DictionaryListViewType.MANAGEABLE_DICTIONARY_VIEW.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < currentList.size) {
            val manageableDictionaryViewHolder = holder as ManageableDictionaryViewHolder
            manageableDictionaryViewHolder.bind(currentList[position])
        } else {
            val newButtonViewHolder = holder as NewButtonViewHolder
            newButtonViewHolder.bind(onNewButtonAction)
        }
    }

    override fun getItemCount(): Int = currentList.size + 1

    fun updateList(content: List<ManageableDictionaryView>) {
        submitList(content)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ManageableDictionaryView>() {
            override fun areItemsTheSame(
                oldItem: ManageableDictionaryView,
                newItem: ManageableDictionaryView
            ): Boolean {
                return oldItem.dictionaryId == newItem.dictionaryId
            }

            override fun areContentsTheSame(
                oldItem: ManageableDictionaryView,
                newItem: ManageableDictionaryView
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

}

enum class DictionaryListViewType(val id: Int) {
    MANAGEABLE_DICTIONARY_VIEW(0),
    NEW_BUTTON_VIEW(1)
}


interface OnDictionaryUpdateListener {
    fun onClick(dictionaryUpdate: DictionaryUpdateUi.Info)
}

interface OnNewButtonAction {
    fun invoke()
}
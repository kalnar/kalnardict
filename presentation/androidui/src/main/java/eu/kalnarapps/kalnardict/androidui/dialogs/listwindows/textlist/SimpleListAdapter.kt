package eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eu.kalnarapps.kalnardict.models.dictionaryquery.ListTextItem

class SimpleListAdapter<T : ListTextItem>(
    private val context: Context,
    private val onClickAction: ((ListTextItem) -> Unit)?
) : BaseAdapter() {
    private var list: List<ListTextItem> = emptyList()
    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is SimpleListItemViewHolder) {
                viewHolder.bind(list[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = SimpleListItemViewHolder(inflater, null, onClickAction)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(list[position])

            convertedView
        }
    }

    override fun getItemViewType(position: Int): Int = 0

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getViewTypeCount(): Int {
        return list.indices.groupBy { getItemViewType(it) }.size
    }

    override fun isEnabled(position: Int): Boolean = true

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun hasStableIds(): Boolean = false

    override fun areAllItemsEnabled(): Boolean = true

    override fun getCount(): Int {
        return list.size
    }

    fun submitListUpdate(it: List<T>) {
        list = it
        notifyDataSetChanged()
    }

}

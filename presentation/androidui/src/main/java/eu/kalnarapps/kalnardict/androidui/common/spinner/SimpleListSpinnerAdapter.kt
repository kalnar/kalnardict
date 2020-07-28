package eu.kalnarapps.kalnardict.androidui.common.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eu.kalnarapps.kalnardict.presentation.models.common.SimpleListItem
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.ListTextItem


class SimpleListSpinnerAdapter(
    private val context: Context,
    private var dictionarySelectorItems: List<SimpleListItem> = emptyList()
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is SimpleListItemViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = SimpleListItemViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is SimpleListItemDropDownViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = SimpleListItemDropDownViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getItem(position: Int): SimpleListItem {
        return dictionarySelectorItems[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dictionarySelectorItems.size
    }

}
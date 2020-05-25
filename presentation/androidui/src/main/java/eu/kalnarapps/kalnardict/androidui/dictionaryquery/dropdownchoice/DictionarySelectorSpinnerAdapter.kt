package eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionarySelectorItem


class DictionarySelectorSpinnerAdapter(
    private val context: Context,
    private val dictionarySelectorItems: List<DictionarySelectorItem> = emptyList()
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is DictionarySelectionViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = DictionarySelectionViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is DictionarySelectorDropDownViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = DictionarySelectorDropDownViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getItem(position: Int): DictionarySelectorItem {
        return dictionarySelectorItems[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dictionarySelectorItems.size
    }
}
package eu.kalnarapps.kalnardict.androidui.dictionaryquery.view.dropdownchoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryUiModel


class DictionarySelectorSpinnerAdapter(
    private val context: Context,
    private val dictionaryUiModels: List<DictionaryUiModel> = emptyList(),
    private val currentDictionaryItemView: DictionaryUiModel?
) : BaseAdapter() {

    // a spinner will always select the first item no matter what inside setAdapter of AbsSpinner
    // that's why we move the current item on the top of the list
    private val dictionaryViewItems: List<DictionaryUiModel>
        get() {
            return currentDictionaryItemView?.let {
                listOf(currentDictionaryItemView).plus(
                    dictionaryUiModels.filterNot { it.id == currentDictionaryItemView.id })
            } ?: dictionaryUiModels
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is DictionarySelectionViewHolder) {
                viewHolder.bind(dictionaryViewItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = DictionarySelectionViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionaryViewItems[position])

            convertedView
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is DictionarySelectorDropDownViewHolder) {
                viewHolder.bind(dictionaryViewItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = DictionarySelectorDropDownViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionaryViewItems[position])

            convertedView
        }
    }

    override fun getItem(position: Int): DictionaryUiModel {
        return dictionaryViewItems[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dictionaryViewItems.size
    }

}
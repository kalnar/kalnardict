package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionarySelectorItem


class LanguageSelectorSpinnerAdapter(
    private val context: Context,
    private val dictionarySelectorItems: List<SelectableLanguage.LanguageUi> = emptyList()
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is LanguageListItemViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = LanguageListItemViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView != null) {
            val viewHolder = convertView.tag
            if (viewHolder is LanguageSelectorDropDownViewHolder) {
                viewHolder.bind(dictionarySelectorItems[position])
            }
            convertView
        } else {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val viewHolder = LanguageSelectorDropDownViewHolder(inflater, null)
            val convertedView = viewHolder.itemView

            convertedView.tag = viewHolder
            viewHolder.bind(dictionarySelectorItems[position])

            convertedView
        }
    }

    override fun getItem(position: Int): SelectableLanguage.LanguageUi {
        return dictionarySelectorItems[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dictionarySelectorItems.size
    }

}
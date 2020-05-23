package eu.kalnarapps.kalnardict.androidui.dictionaryquery.dropdownchoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.DictionarySelectorItem

class DictionarySelectorSpinner(
    private val context: Context,
    private val dictionarySelectorItems: List<DictionarySelectorItem> = emptyList()
) : BaseAdapter() {
    private val itemLayout: Int = R.layout.query_language_selector_item

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // TODO(use viewholder pattern?)
        return LayoutInflater.from(context).inflate(itemLayout, null).apply {
            val titleView = findViewById<TextView>(R.id.selector_title)
            titleView.text = dictionarySelectorItems[position].displayString
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
package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.ExternalTableUiInfo

class TableInfoViewHolder(
    inflater: LayoutInflater, parent: ViewGroup,
    private val onTableInfoClickListener: OnTableInfoClickListener
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_registry_table_info_item_view,
        parent,
        false
    )
) {
    private val titleView: TextView = itemView.findViewById(R.id.dictionary_name)
    private val languageFromView: TextView = itemView.findViewById(R.id.language_form)
    private val languageToView: TextView = itemView.findViewById(R.id.language_to)


    fun bind(manageableDictionaryView: ExternalTableUiInfo) {
        titleView.text = manageableDictionaryView.dictionaryName
        languageFromView.text = manageableDictionaryView.originalLanguageFrom
        languageToView.text = manageableDictionaryView.originalLanguageTo
        titleView.setOnClickListener {
            onTableInfoClickListener.onClick(manageableDictionaryView)
        }
    }
}
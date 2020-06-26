package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
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
    private val languageFromView: TextView = itemView.findViewById(R.id.language_from_key)
    private val languageToView: TextView = itemView.findViewById(R.id.language_to_key)
    private val dictionaryNameEditText: TextInputEditText =
        itemView.findViewById(R.id.dictionary_name_key_edit)


    fun bind(tableInfoUi: ExternalTableUiInfo) {
        titleView.text = tableInfoUi.dictionaryName
        dictionaryNameEditText.hint = tableInfoUi.dictionaryName
        languageFromView.text = tableInfoUi.originalLanguageFrom
        languageToView.text = tableInfoUi.originalLanguageTo
        titleView.setOnClickListener {
            onTableInfoClickListener.onClick(tableInfoUi)
        }
    }
}
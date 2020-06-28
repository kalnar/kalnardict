package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage

class TableInfoListAdapter(
    private val list: List<ExternalTableUiInfo>,
    private val languageList: List<SelectableLanguage.LanguageUi>,
    private val onRegisterTablesListener: OnRegisterTablesListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return TableInfoViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onRegisterTablesListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val manageableDictionaryViewHolder = holder as TableInfoViewHolder
        manageableDictionaryViewHolder.bind(list[position], languageList)
    }

    override fun getItemCount(): Int = list.size

}

interface OnRegisterTablesListener {
    fun onChanged(newTableInfoUiModel: ExternalTableUiInfo)
}


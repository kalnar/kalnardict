package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info.language.RegisteredLanguageItemUiModel

class TableInfoListAdapter(
    private val list: List<ExternalTableUiInfo>,
    private val languageList: List<RegisteredLanguageItemUiModel>,
    private val onTableInfoClickListener: OnTableInfoClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return TableInfoViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onTableInfoClickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val manageableDictionaryViewHolder = holder as TableInfoViewHolder
        manageableDictionaryViewHolder.bind(list[position], languageList)
    }

    override fun getItemCount(): Int = list.size

}

interface OnTableInfoClickListener {
    fun onClick(dictionaryView: ExternalTableUiInfo)
}

interface OnNewButtonAction {
    fun invoke()
}
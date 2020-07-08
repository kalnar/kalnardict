package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage

class TableInfoListAdapter(
    list: List<ExternalTableUiInfo>,
    private var languageList: List<SelectableLanguage.LanguageUi>,
    private val onRegisterTablesListener: OnRegisterTablesListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallBack =
        object : DiffUtil.ItemCallback<ExternalTableUiInfo>() {
            override fun areItemsTheSame(
                oldItem: ExternalTableUiInfo,
                newItem: ExternalTableUiInfo
            ): Boolean {
                return oldItem.originalTableName == newItem.originalTableName
            }

            override fun areContentsTheSame(
                oldItem: ExternalTableUiInfo,
                newItem: ExternalTableUiInfo
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val differ: AsyncListDiffer<ExternalTableUiInfo> = AsyncListDiffer(
        this,
        diffCallBack
    )

    init {
        differ.submitList(list)
    }

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
        manageableDictionaryViewHolder.bind(differ.currentList[position], languageList)
    }


    override fun getItemCount(): Int = differ.currentList.size

    fun updateLanguageList(
        it: List<SelectableLanguage.LanguageUi>,
        tables: List<ExternalTableUiInfo>
    ) {
        if (languageList.size != it.size) {
            languageList = it
            differ.submitList(tables)
        }
    }

}

interface OnRegisterTablesListener {
    fun onChanged(newTableInfoUiModel: ExternalTableUiInfo)
}


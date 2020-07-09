package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.table.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.RegisterDictionaryUi
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage

class TableInfoListAdapter(
    list: List<RegisterDictionaryUi>,
    private val onRegisterInfoUpdateListener: OnRegisterInfoUpdateListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallBack =
        object : DiffUtil.ItemCallback<RegisterDictionaryUi>() {
            override fun areItemsTheSame(
                oldItem: RegisterDictionaryUi,
                newItem: RegisterDictionaryUi
            ): Boolean {
                return oldItem.tableUiInfo.originalTableName ==
                        newItem.tableUiInfo.originalTableName
            }

            override fun areContentsTheSame(
                oldItem: RegisterDictionaryUi,
                newItem: RegisterDictionaryUi
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val differ: AsyncListDiffer<RegisterDictionaryUi> = AsyncListDiffer(
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
            onRegisterInfoUpdateListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val manageableDictionaryViewHolder = holder as TableInfoViewHolder
        manageableDictionaryViewHolder.bind(differ.currentList[position])
    }


    override fun getItemCount(): Int = differ.currentList.size

    fun updateList(
        newList: List<RegisterDictionaryUi>
    ) {
        if (newList.isNotEmpty() &&
            differ.currentList.availableLanguages.size !=
            newList.availableLanguages.size
        )
            differ.submitList(newList)
    }

}

private val List<RegisterDictionaryUi>.availableLanguages: List<SelectableLanguage.LanguageUi>
    get() = flatMap { it.availableLanguages }.distinctBy { it.code }

interface OnRegisterInfoUpdateListener {
    fun onChanged(newTableInfoUiModel: ExternalTableUiInfo)
}


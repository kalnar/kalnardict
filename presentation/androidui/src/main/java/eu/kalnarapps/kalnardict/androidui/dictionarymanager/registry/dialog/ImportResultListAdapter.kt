package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableStatus
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

class ImportResultListAdapter(
    private var list: List<ImportTableStatus>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            StatusViewType.SUCCESS_STATUS_VIEW.id -> SuccessfulImportResultViewHolder(
                inflater,
                parent
            )
            StatusViewType.LOADING_STATUS_VIEW.id -> ProcessingImportViewHolder(
                inflater,
                parent
            )
            else -> FailedImportResultViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val importResultViewHolder = holder as ImportResultViewHolder
        importResultViewHolder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (val progressStatus = list[position].progress) {
            is DataOperationResult.Success -> {
                if (progressStatus.data.totalRows == progressStatus.data.registeredRows) {
                    StatusViewType.SUCCESS_STATUS_VIEW.id
                } else {
                    StatusViewType.LOADING_STATUS_VIEW.id
                }
            }
            is DataOperationResult.Failure -> StatusViewType.FAILURE_STATUS_VIEW.id
        }
    }

    override fun getItemCount(): Int = list.size

    fun update(it: List<ImportTableStatus>) {
        list = it
        notifyDataSetChanged()
    }

}

enum class StatusViewType(val id: Int) {
    SUCCESS_STATUS_VIEW(0),
    LOADING_STATUS_VIEW(1),
    FAILURE_STATUS_VIEW(2)
}

interface ImportResultViewHolder {
    fun bind(importTableResult: ImportTableStatus)
}
package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ImportTableResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult

class ImportResultListAdapter(
    private val list: List<ImportTableResult>
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
            else -> FailedImportResultViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val importResultViewHolder = holder as ImportResultViewHolder
        importResultViewHolder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].result) {
            OperationResult.Success -> StatusViewType.SUCCESS_STATUS_VIEW.id
            is OperationResult.Failure -> StatusViewType.FAILURE_STATUS_VIEW.id
        }
    }

    override fun getItemCount(): Int = list.size

}

enum class StatusViewType(val id: Int) {
    SUCCESS_STATUS_VIEW(0),
    FAILURE_STATUS_VIEW(1)
}

interface ImportResultViewHolder {
    fun bind(importTableResult: ImportTableResult)
}
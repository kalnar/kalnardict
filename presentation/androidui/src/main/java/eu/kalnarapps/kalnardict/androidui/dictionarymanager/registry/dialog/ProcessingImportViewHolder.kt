package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ImportTableStatus
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

class ProcessingImportViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_registry_dialog_importing_item_view,
        parent,
        false
    )
), ImportResultViewHolder {
    private val statusTextView: TextView = itemView.findViewById(R.id.import_result_text)
    private val progressTextView: TextView = itemView.findViewById(R.id.progress_text)
    private val progressBarHorizontal: ProgressBar =
        itemView.findViewById(R.id.progress_bar_horizontal)


    override fun bind(
        importTableResult: ImportTableStatus
    ) {
        statusTextView.text = statusTextView.context.getString(
            R.string.table_registration_dialog_processing_status_message,
            importTableResult.table.originalTableName
        )
        when (importTableResult.progress) {
            is DataOperationResult.Success -> {
                progressTextView.text =
                    "${importTableResult.progress.data.registeredRows}/" +
                            "${importTableResult.progress.data.totalRows}"
                if (progressBarHorizontal.max != importTableResult.progress.data.totalRows) {
                    progressBarHorizontal.max = importTableResult.progress.data.totalRows
                }
                progressBarHorizontal.progress = importTableResult.progress.data.registeredRows
            }
            is DataOperationResult.Failure -> Unit
        }.exhaustive
    }

}

package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableStatus

class SuccessfulImportResultViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.dictionary_registry_dialog_successful_import_result_item_view,
        parent,
        false
    )
), ImportResultViewHolder {
    private val statusTextView: TextView = itemView.findViewById(R.id.import_result_text)

    override fun bind(
        importTableResult: ImportTableStatus
    ) {
        statusTextView.text = statusTextView.context.getString(
            R.string.table_registration_dialog_success_status_message,
            importTableResult.table.originalTableName,
            importTableResult.table.dictionaryName
        )
    }

}

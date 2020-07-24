package eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.ListTextItem

class SimpleListItemViewHolder(
    inflater: LayoutInflater, parent: ViewGroup?,
    private val onClickAction: ((ListTextItem) -> Unit)?
) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.simple_popup_list_item_text,
        parent,
        false
    )
) {
    private val baseFormView: TextView = itemView.findViewById(R.id.simple_item_view)
    private val container: LinearLayout =
        itemView.findViewById(R.id.simple_item_container)

    fun bind(queryModeUiModel: ListTextItem) {
        baseFormView.text = queryModeUiModel.displayString
        onClickAction?.let { action ->
            container.setOnClickListener {
                action(queryModeUiModel)
            }
        }
        if (queryModeUiModel.isSelected) {
            container.background =
                ContextCompat.getDrawable(container.context, R.drawable.primary_secondary_gradient_bg)
        } else {
            container.background =
                ContextCompat.getDrawable(container.context, R.color.purple_dark)
        }
    }
}

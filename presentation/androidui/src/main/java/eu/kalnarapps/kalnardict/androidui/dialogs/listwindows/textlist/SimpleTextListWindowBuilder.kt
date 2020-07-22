package eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist

import android.content.Context
import android.widget.ListAdapter
import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.dialogs.DialogBuilder
import eu.kalnarapps.kalnardict.androidui.dialogs.ListDialog
import eu.kalnarapps.kalnardict.models.dictionaryquery.ListTextItem

class SimpleTextListWindowBuilder(
    private val context: Context,
    private val adapter: ListAdapter
) :
    DialogBuilder<ListTextItem>(listAdapter = adapter) {
    override fun build(): ListDialog<ListTextItem> {

        return SimpleListWindow<ListTextItem>(
            context = context
        ).apply {
            anchorView = this@SimpleTextListWindowBuilder.anchorView
            isModal = true
            setAdapter(adapter)
            height = ListPopupWindow.WRAP_CONTENT
            val drawable = ContextCompat.getDrawable(context, R.drawable.bg_dialog_bubble)
            setBackgroundDrawable(drawable)
        }
    }
}

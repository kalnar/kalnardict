package eu.kalnarapps.kalnardict.androidui.dialogs.listwindows.textlist

import android.content.Context
import androidx.appcompat.widget.ListPopupWindow
import eu.kalnarapps.kalnardict.androidui.dialogs.ListDialog

class SimpleListWindow<T>(context: Context) : ListPopupWindow(context),
    ListDialog<T> {
    override fun dismiss() {
        super.dismiss()
    }
}



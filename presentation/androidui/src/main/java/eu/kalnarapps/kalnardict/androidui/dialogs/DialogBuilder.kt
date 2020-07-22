package eu.kalnarapps.kalnardict.androidui.dialogs

import android.view.View
import android.widget.ListAdapter

interface ListDialog<T> {
    fun show()
    fun dismiss()
}

abstract class DialogBuilder<T>(
    protected var anchorView: View? = null,
    protected var listAdapter: ListAdapter
) {
    fun withAnchor(anchor: View) = apply { this.anchorView = anchor }

    abstract fun build(): ListDialog<T>
    fun show() = build().show()
}

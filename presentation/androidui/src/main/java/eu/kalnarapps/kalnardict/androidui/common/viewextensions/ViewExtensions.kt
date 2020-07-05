package eu.kalnarapps.kalnardict.androidui.common.viewextensions

import android.view.View


fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
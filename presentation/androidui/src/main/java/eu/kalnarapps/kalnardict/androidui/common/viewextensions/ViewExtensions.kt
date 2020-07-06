package eu.kalnarapps.kalnardict.androidui.common.viewextensions

import android.view.View


fun View.visibleXorGone(condition: Boolean) {
    visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.visibleXorInvisible(condition: Boolean) {
    visibility = if (condition) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

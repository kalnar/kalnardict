package eu.kalnarapps.kalnardict.androidtest

import android.content.Context

fun Context.getStorageRootPath(): String {
    return (getExternalFilesDir(null)?.absolutePath
        ?: filesDir.absolutePath).substringBefore("Android")
}

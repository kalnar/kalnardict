package eu.kalnarapps.kalnardict.android.utils

import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi

interface UiLogger {
    fun log(msg: String)
    fun d(tag: String, msg: String)
    fun logErrorFromUi(it: ErrorFromUi)
    fun logObject(data: Any?)
}
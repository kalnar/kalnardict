package eu.kalnarapps.kalnardict.android.utils

import android.util.Log
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi

class KalnarLogger : Logger {
    override fun log(msg: String) {
        Log.d("kalnarLogger", msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun logErrorFromUi(it: ErrorFromUi) {
        Log.d(ERROR_FROM_UI_TAG, it.logMessage)
    }

    companion object {
        const val ERROR_FROM_UI_TAG = "kd.error.ui"
    }
}
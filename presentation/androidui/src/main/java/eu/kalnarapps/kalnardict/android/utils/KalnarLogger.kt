package eu.kalnarapps.kalnardict.android.utils

import android.util.Log

class KalnarLogger : Logger {
    override fun log(msg: String) {
        Log.d("kalnarLogger", msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}
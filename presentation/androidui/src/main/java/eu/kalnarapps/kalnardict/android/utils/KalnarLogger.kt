package eu.kalnarapps.kalnardict.android.utils

import android.util.Log

class KalnarLogger : Logger {
    override fun log(msg: String) {
        Log.d("kalnarLogger", msg)
    }
}
package eu.kalnarapps.kalnardict.android.utils

import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi


class KalnarUiLogger : UiLogger {

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(3) // (Optional) How many method line to show. Default 2
            .methodOffset(0) // (Optional) Hides internal method calls up to offset. Default 5
//            .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("kalnarDict") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

    }


    override fun log(msg: String) {
//        Log.d("kalnarLogger", msg)
        Logger.d(msg)
    }

    override fun d(tag: String, msg: String) {
        Logger.log(Logger.DEBUG, tag, msg, null)
    }

    override fun logErrorFromUi(it: ErrorFromUi) {
        Log.d(ERROR_FROM_UI_TAG, it.logMessage)
    }

    override fun logObject(data: Any?) {
        // TODO: use moshi or something else to convert to json then pretty print with logger
        Logger.d(data)
    }

    companion object {
        const val ERROR_FROM_UI_TAG = "kd.error.ui"
    }
}
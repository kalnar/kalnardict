package eu.kalnarapps.kalnardict.androidui.test

import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi


class TestLogger : UiLogger {
    override fun log(msg: String) {
        println(msg)
    }

    override fun d(tag: String, msg: String) {
        println("[$tag]:$msg")
    }

    override fun logErrorFromUi(it: ErrorFromUi) {
        println(it.logMessage)
    }

    override fun logObject(data: Any?) {
        println("data:${data.toString()}")
    }

}
package eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers

import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi

interface UiLogger {
    fun log(msg: String)
    fun d(tag: String, msg: String)
    fun logErrorFromUi(it: ErrorFromUi)
    fun logObject(data: Any?)
}
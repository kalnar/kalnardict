package eu.kalnarapps.kalnardict.android.utils.error

import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand

data class ErrorFromUi(
    val logMessage: String,
    val errorFeedback: ErrorUiFeedBack = ErrorUiFeedBack.OnlyLog
)

sealed class ErrorUiFeedBack {
    class ShowSnackBar(val msg: String) : ErrorUiFeedBack()
    class Navigate(val navCommand: NavigationCommand) : ErrorUiFeedBack()
    object OnlyLog : ErrorUiFeedBack()
}

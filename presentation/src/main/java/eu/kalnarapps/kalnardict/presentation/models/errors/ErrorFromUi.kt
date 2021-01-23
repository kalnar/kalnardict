package eu.kalnarapps.kalnardict.presentation.models.errors

import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand

data class ErrorFromUi(
    val logMessage: String,
    val errorFeedback: ErrorUiFeedBack = ErrorUiFeedBack.OnlyLog
)

sealed class ErrorUiFeedBack {
    class ShowSnackBar(val msg: String) : ErrorUiFeedBack()
    class ShowToast(val msg: String) : ErrorUiFeedBack()
    class Navigate(val navCommand: NavigationCommand) : ErrorUiFeedBack()
    object OnlyLog : ErrorUiFeedBack()
}

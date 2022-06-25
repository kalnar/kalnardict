package eu.kalnarapps.kalnardict.presentation.models.errors

import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand

data class ErrorFromUi(
    val logMessage: String,
    val errorFeedback: ErrorUiFeedBack = ErrorUiFeedBack.OnlyLog
)

sealed class ErrorUiFeedBack {
    class ShowSnackBar(val msg: String) : ErrorUiFeedBack()

    class ShowSnackBarWithNavigation(
        val msg: String,
        val actionLabel: String,
        val action: NavigationCommand
    ) : ErrorUiFeedBack()

    class ShowSnackBarWithAction(
        val msg: String,
        val actionLabel: String,
        val action: () -> Unit
    ) : ErrorUiFeedBack() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ShowSnackBarWithAction

            if (msg != other.msg) return false
            if (actionLabel != other.actionLabel) return false

            return true
        }

        override fun hashCode(): Int {
            var result = msg.hashCode()
            result = 31 * result + actionLabel.hashCode()
            return result
        }
    }

    class ShowToast(val msg: String) : ErrorUiFeedBack()
    class Navigate(val navCommand: NavigationCommand) : ErrorUiFeedBack()
    object OnlyLog : ErrorUiFeedBack()
}

sealed class UiFeedback {
    data class ShowSuccessSnackBar(val msg: String) : UiFeedback()
}
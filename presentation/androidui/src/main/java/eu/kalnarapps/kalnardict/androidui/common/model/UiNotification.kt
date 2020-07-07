package eu.kalnarapps.kalnardict.androidui.common.model

open class UiNotification(
    private val isAlive: Boolean = false
) {
    private var isHandled: Boolean = false

    fun handled() {
        isHandled = true
    }

    fun toBeHandled(): Boolean = !isHandled && isAlive
}

class LiveUiNotification() : UiNotification(isAlive = true)
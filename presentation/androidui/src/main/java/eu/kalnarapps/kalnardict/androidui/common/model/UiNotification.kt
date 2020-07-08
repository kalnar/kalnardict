package eu.kalnarapps.kalnardict.androidui.common.model

import androidx.lifecycle.Observer

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

class UiNotificationObserver(
    private val onObserveNotification: () -> Unit
) : Observer<UiNotification> {
    override fun onChanged(t: UiNotification) {
        if (t.toBeHandled()) {
            onObserveNotification()
        }
    }

}


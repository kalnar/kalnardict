package eu.kalnarapps.kalnardict.androidui.common.model

import androidx.lifecycle.Observer


class ChangeObserver<T>(
    private val onObserveAction: (T) -> Unit
) : Observer<T> {

    private var lastObservedObject: T? = null

    override fun onChanged(t: T) {
        if (lastObservedObject != t) {
            lastObservedObject = t
            onObserveAction(t)
        }
    }

}
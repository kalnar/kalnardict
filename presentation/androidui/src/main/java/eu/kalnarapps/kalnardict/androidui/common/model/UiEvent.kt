package eu.kalnarapps.kalnardict.androidui.common.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class UiEvent<T : Any>(
    private val mContent: T
) {

    @VisibleForTesting
    fun pureContent(): T {
        return mContent
    }


    fun content(): T {
        hasBeenHandled = true
        return mContent
    }

    private var hasBeenHandled = false

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }

    override fun toString(): String {
        return "UiEvent($mContent; hasBeenHandled: $hasBeenHandled)"
    }

}

sealed class EventContent {
    object ContentAlreadyHandled : EventContent()
    data class ContentToHandle<T : Any>(val content: T) : EventContent()
}


class UiEventObserver<T : Any>(
    private val onObserveAction: (T) -> Unit
) : Observer<UiEvent<T>> {
    override fun onChanged(t: UiEvent<T>) {
        if (!t.hasBeenHandled()) {
            onObserveAction(t.content())
        }
    }

}

class NewChangeObserver<T : Any>(
    private val onObserveAction: (T) -> Unit
) : Observer<T> {
    private var isFirstAlreadyIgnored = false
    override fun onChanged(t: T) {
        if (isFirstAlreadyIgnored) {
            onObserveAction(t)
        } else {
            isFirstAlreadyIgnored = true
        }
    }

}

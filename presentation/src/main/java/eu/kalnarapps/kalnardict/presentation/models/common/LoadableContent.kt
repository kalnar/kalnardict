package eu.kalnarapps.kalnardict.presentation.models.common

import eu.kalnarapps.kalnardict.common.operations.OperationFailure

sealed class LoadableContent<out T> {
    object UnInitialized : LoadableContent<Nothing>()
    object Loading : LoadableContent<Nothing>()
    data class Failed(val failure: OperationFailure) : LoadableContent<Nothing>()
    data class Completed<T>(val content: T) : LoadableContent<T>()
}

fun <T> LoadableContent<T>.contentOrNull(): T? {
    return if (this is LoadableContent.Completed) {
        content
    } else {
        null
    }
}

data class LoadingProgress(
    val percentage: Int
)

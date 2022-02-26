package eu.kalnarapps.kalnardict.presentation.models.common

sealed class LoadableContent<out T> {
    object UnInitialized : LoadableContent<Nothing>()
    object Loading : LoadableContent<Nothing>()
    data class Completed<T>(val content: T) : LoadableContent<T>()
}

data class LoadingProgress(
    val percentage: Int
)

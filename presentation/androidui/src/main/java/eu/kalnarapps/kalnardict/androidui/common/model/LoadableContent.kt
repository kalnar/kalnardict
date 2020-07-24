package eu.kalnarapps.kalnardict.androidui.common.model

sealed class LoadableContent<out T> {
    object UnInitialized : LoadableContent<Nothing>()
    object Loading : LoadableContent<Nothing>()
    class Completed<T>(val content: T) : LoadableContent<T>()
}

data class LoadingProgress(
    val percentage: Int
)

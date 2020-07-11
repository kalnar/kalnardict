package eu.kalnarapps.kalnardict.androidui.common.model

sealed class LoadableContent<out T> {
    object UnInitialized : LoadableContent<Nothing>()
    class Loading<T>(val progress: LoadingProgress) : LoadableContent<T>()
    class Completed<T>(val content: T) : LoadableContent<T>()
}

data class LoadingProgress(
    val percentage: Int
)

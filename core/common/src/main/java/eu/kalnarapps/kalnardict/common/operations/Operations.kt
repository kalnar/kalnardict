package eu.kalnarapps.kalnardict.common.operations


sealed class OperationResult {
    object Success : OperationResult()
    data class Failure(val errorMessage: String, val cause: OperationFailure? = null) :
        OperationResult(),
        OperationFailure {
        override fun errorMessage(): String {
            return errorMessage + "\ncause: ${cause?.errorMessage() ?: "source cause"}"
        }
    }
}

interface OperationFailure {
    fun errorMessage(): String
}

sealed class DataOperationResult<T> {
    data class Success<T>(val data: T) : DataOperationResult<T>()
    data class Failure<T>(val errorMessage: String, val cause: OperationFailure? = null) :
        DataOperationResult<T>(), OperationFailure {
        override fun errorMessage(): String {
            return errorMessage + "\ncause: ${cause?.errorMessage() ?: "source cause"}"
        }
    }
}

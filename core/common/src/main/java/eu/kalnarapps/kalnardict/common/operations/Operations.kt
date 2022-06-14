package eu.kalnarapps.kalnardict.common.operations

import java.lang.Exception


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
    fun <R> map(transformation: (T) -> R): DataOperationResult<R> {
        return when (this) {
            is Success -> Success(transformation(this.data))
            is Failure -> Failure(errorMessage = this.errorMessage, cause = this.cause)
        }
    }

    data class Success<T>(val data: T) : DataOperationResult<T>()
    data class Failure<T>(val errorMessage: String, val cause: OperationFailure? = null) :
        DataOperationResult<T>(), OperationFailure {
        override fun errorMessage(): String {
            return errorMessage + "\ncause: ${cause?.errorMessage() ?: "source cause"}"
        }
    }

    companion object {

        fun <T> firstFailure(vararg results: DataOperationResult<*>): Failure<T> {
            val failures = results.filterIsInstance(Failure::class.java)
            return if (failures.isNotEmpty()) {
                Failure<T>(
                    errorMessage = "an error has occurred",
                    // TODO: create a combined failure object with list of causes
                    cause = failures.first()
                )
            } else {
                Failure<T>("tried to combine failures when there were none")
            }
        }
    }
}

data class OperationException(val exception: Exception) : OperationFailure {
    override fun errorMessage(): String {
        return exception.stackTraceToString()
    }
}
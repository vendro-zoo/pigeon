package it.zoo.vendro.pigeon.result

import it.zoo.vendro.pigeon.exception.EndpointException
import org.jetbrains.annotations.Contract

class EndpointResult<T>(
    /**
     * The status of the result.
     */
    val status: EndpointResultStatus = EndpointResultStatus.OK,
    val message: String?,
    val data: T?
) {
    fun throwIfError() {
        if (status == EndpointResultStatus.ERROR)
            throw EndpointException(message)
    }

    fun getOrThrow(): T? {
        throwIfError()
        return data
    }
}
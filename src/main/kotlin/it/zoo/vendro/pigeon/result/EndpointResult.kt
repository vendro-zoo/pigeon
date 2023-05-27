package it.zoo.vendro.pigeon.result

import it.zoo.vendro.pigeon.exception.EndpointException

class EndpointResult<T>(
    /**
     * The status of the result.
     */
    val status: EndpointResultStatus = EndpointResultStatus.OK,
    val message: String?,
    val data: T?
) {
    fun tryThrow() {
        if (status == EndpointResultStatus.ERROR)
            throw EndpointException(message)
    }

    fun unwrap(): T? {
        tryThrow()
        return data
    }
}
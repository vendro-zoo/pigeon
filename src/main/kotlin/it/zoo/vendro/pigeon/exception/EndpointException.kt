package it.zoo.vendro.pigeon.exception

import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.EndpointResultStatus

class EndpointException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    constructor(message: String) : this(message, null)

    fun toResult() = toTypedResult<Unit>()

    fun <T> toTypedResult() = EndpointResult<T>(
        status = EndpointResultStatus.ERROR,
        message = message,
        data = null
    )
}
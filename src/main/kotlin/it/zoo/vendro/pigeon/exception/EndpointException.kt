package it.zoo.vendro.pigeon.exception

import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.TypedEndpointResult

class EndpointException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    constructor(message: String) : this(message, null)

    fun toResult(): EndpointResult<Nothing> = TypedEndpointResult.Error(error = this)
}
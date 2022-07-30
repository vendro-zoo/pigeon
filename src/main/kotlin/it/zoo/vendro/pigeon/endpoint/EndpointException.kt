package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.response.Response

class EndpointException(message: String?) : Exception(message) {
    fun toResponse() = Response.error<Nothing>(message)
}
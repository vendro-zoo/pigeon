package it.zoo.vendro.pigeon.endpoint.expansions

import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.EndpointResultStatus

interface ResultExpansion {
    fun <T> success(
        data: T? = null,
        message: String? = null,
    ) = EndpointResult(
        status = EndpointResultStatus.OK,
        data = data,
        message = message
    )

    fun <T> error(
        message: String? = null,
        data: T? = null,
    ) = EndpointResult(
        status = EndpointResultStatus.ERROR,
        data = data,
        message = message
    )
}
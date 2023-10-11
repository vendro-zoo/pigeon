package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.TypedEndpointResult

suspend fun <I, M, P, O> Endpoint<*, *, *, *>.chainFullEndpoint(
    nextEndpoint: Endpoint<I, M, P, O>,
    input: I
): EndpointResult<O> {
    nextEndpoint.context = context
    val manipulated = nextEndpoint.manipulateWrapper(TypedEndpointResult.Ok(input), context)
    val processed = nextEndpoint.processWrapper(manipulated, context)
    return nextEndpoint.respondWrapper(processed, context)
}

suspend fun <M, P, O> Endpoint<*, *, *, *>.chainManipulatedEndpoint(
    nextEndpoint: Endpoint<*, M, P, O>,
    manipulated: M
): EndpointResult<O> {
    nextEndpoint.context = context
    val processed = nextEndpoint.processWrapper(TypedEndpointResult.Ok(manipulated), context)
    return nextEndpoint.respondWrapper(processed, context)
}

suspend fun <I, M, P> Endpoint<*, *, *, *>.chainProcessedEndpoint(
    nextEndpoint: Endpoint<I, M, P, *>,
    input: I
): EndpointResult<P> {
    nextEndpoint.context = context
    val manipulated = nextEndpoint.manipulateWrapper(TypedEndpointResult.Ok(input), context)
    return nextEndpoint.processWrapper(manipulated, context)
}

suspend fun <M, P> Endpoint<*, *, *, *>.chainEndpoint(
    nextEndpoint: Endpoint<*, M, P, *>,
    manipulated: M
): EndpointResult<P> {
    nextEndpoint.context = context
    return nextEndpoint.processWrapper(TypedEndpointResult.Ok(manipulated), context)
}
package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.result.EndpointResult

suspend fun <I, M, P, O> Endpoint<*, *, *, *>.chainFullEndpoint(
    nextEndpoint: Endpoint<I, M, P, O>,
    input: I
): O? =
    chainFullEndpointWrapped(nextEndpoint, input).getOrThrow()

suspend fun <M, P, O> Endpoint<*, *, *, *>.chainManipulatedEndpoint(
    nextEndpoint: Endpoint<*, M, P, O>,
    manipulated: M
): O? =
    chainManipulatedEndpointWrapped(nextEndpoint, manipulated).getOrThrow()

suspend fun <I, M, P> Endpoint<*, *, *, *>.chainProcessedEndpoint(
    nextEndpoint: Endpoint<I, M, P, *>,
    input: I
): P? =
    chainProcessedEndpointWrapped(nextEndpoint, input).getOrThrow()

suspend fun <M, P> Endpoint<*, *, *, *>.chainEndpoint(
    nextEndpoint: Endpoint<*, M, P, *>,
    manipulated: M
): P? =
    chainEndpointWrapped(nextEndpoint, manipulated).getOrThrow()

suspend fun <I, M, P, O> Endpoint<*, *, *, *>.chainFullEndpointWrapped(
    nextEndpoint: Endpoint<I, M, P, O>,
    input: I
): EndpointResult<O> =
    Endpoint.toResponse {
        val manipulated = nextEndpoint.manipulateWrapper(input, context)
        val processed = nextEndpoint.processWrapper(manipulated, context)
        nextEndpoint.respondWrapper(processed, context)
    }

suspend fun <M, P, O> Endpoint<*, *, *, *>.chainManipulatedEndpointWrapped(
    nextEndpoint: Endpoint<*, M, P, O>,
    manipulated: M
): EndpointResult<O> =
    Endpoint.toResponse {
        val processed = nextEndpoint.processWrapper(manipulated, context)
        nextEndpoint.respondWrapper(processed, context)
    }

suspend fun <I, M, P> Endpoint<*, *, *, *>.chainProcessedEndpointWrapped(
    nextEndpoint: Endpoint<I, M, P, *>,
    input: I
): EndpointResult<P> =
    Endpoint.toResponse {
        val manipulated = nextEndpoint.manipulateWrapper(input, context)
        nextEndpoint.processWrapper(manipulated, context)
    }

suspend fun <M, P> Endpoint<*, *, *, *>.chainEndpointWrapped(
    nextEndpoint: Endpoint<*, M, P, *>,
    manipulated: M
): EndpointResult<P> =
    Endpoint.toResponse {
        nextEndpoint.processWrapper(manipulated, context)
    }
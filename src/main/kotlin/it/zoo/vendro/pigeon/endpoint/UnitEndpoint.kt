package it.zoo.vendro.pigeon.endpoint

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.TypedEndpointResult
import kotlin.reflect.typeOf

abstract class UnitEndpoint : Endpoint<Unit, Unit, Unit, Unit>(
    inputType = typeOf<Unit>(),
    manipulatedType = typeOf<Unit>(),
    processedType = typeOf<Unit>(),
    outputType = typeOf<Unit>(),
) {
    override suspend fun transform(call: ApplicationCall, context: EndpointContext) = TypedEndpointResult.Ok(Unit)

    override suspend fun respond(processed: EndpointResult<Unit>, context: EndpointContext): EndpointResult<Unit> =
        processed

    override suspend fun manipulate(input: EndpointResult<Unit>, context: EndpointContext) = input
}
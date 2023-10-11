package it.zoo.vendro.pigeon.endpoint

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.result.TypedEndpointResult
import kotlin.reflect.KType
import kotlin.reflect.typeOf

abstract class SupplierEndpoint<P, O>(
    processedType: KType,
    outputType: KType
) : Endpoint<Unit?, Unit?, P, O>(
    inputType = typeOf<Unit?>(),
    manipulatedType = typeOf<Unit?>(),
    processedType = processedType,
    outputType = outputType
) {
    override suspend fun transform(call: ApplicationCall, context: EndpointContext) = TypedEndpointResult.Ok(null)
    override suspend fun manipulate(input: EndpointResult<Unit?>, context: EndpointContext) = input
}
package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult
import kotlin.reflect.KType

abstract class BasicEndpoint<I, O>(
    inputType: KType,
    outputType: KType
) : Endpoint<I, I, O, O>(
    inputType = inputType,
    manipulatedType = inputType,
    processedType = outputType,
    outputType = outputType
) {
    override suspend fun manipulate(input: EndpointResult<I>, context: EndpointContext) = input
    override suspend fun respond(processed: EndpointResult<O>, context: EndpointContext) = processed
}
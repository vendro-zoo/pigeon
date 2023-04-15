package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult
import kotlin.reflect.KType
import kotlin.reflect.typeOf

abstract class ConsumerEndpoint<I, M>(
    processedType: KType,
    outputType: KType
) : Endpoint<I, M, Unit?, Unit?>(
    inputType = typeOf<Unit?>(),
    manipulatedType = typeOf<Unit?>(),
    processedType = processedType,
    outputType = outputType
) {
    override suspend fun respond(processed: EndpointResult<Unit?>, context: EndpointContext): EndpointResult<Unit?> = processed
}
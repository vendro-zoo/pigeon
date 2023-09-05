package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult
import kotlin.reflect.KType
import kotlin.reflect.typeOf

abstract class ConsumerEndpoint<I, M>(
    inputType: KType,
    manipulatedType: KType
) : Endpoint<I, M, Unit?, Unit?>(
    inputType = inputType,
    manipulatedType = manipulatedType,
    processedType = typeOf<Unit?>(),
    outputType = typeOf<Unit?>(),
) {
    override suspend fun respond(processed: EndpointResult<Unit?>, context: EndpointContext): EndpointResult<Unit?> = processed
}
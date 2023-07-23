package it.zoo.vendro.pigeon.wrapping

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult

interface EndpointCallWrapper {
    suspend fun onCall(
        chain: EndpointCallWrapperChain,
        call: ApplicationCall,
        context: EndpointContext,
        block: suspend (call: ApplicationCall, context: EndpointContext) -> EndpointResult<*>
    ) = block(call, context)

    suspend fun onStart(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterTransform(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterManipulate(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterProcess(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterRespond(call: ApplicationCall, context: EndpointContext, result: EndpointResult<*>) {}
}
package it.zoo.vendro.pigeon.wrapping

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext

interface EndpointCallWrapper {
    suspend fun onCall(
        chain: EndpointCallWrapperChain,
        call: ApplicationCall,
        context: EndpointContext,
        block: suspend (call: ApplicationCall, context: EndpointContext) -> Unit
    ) = block(call, context)

    suspend fun beforeTransform(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterTransform(call: ApplicationCall, context: EndpointContext) {}
    suspend fun beforeManipulate(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterManipulate(call: ApplicationCall, context: EndpointContext) {}
    suspend fun beforeProcess(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterProcess(call: ApplicationCall, context: EndpointContext) {}
    suspend fun beforeRespond(call: ApplicationCall, context: EndpointContext) {}
    suspend fun afterRespond(call: ApplicationCall, context: EndpointContext) {}
}
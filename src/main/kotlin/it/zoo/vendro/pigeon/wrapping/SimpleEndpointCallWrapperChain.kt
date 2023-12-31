package it.zoo.vendro.pigeon.wrapping

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.result.EndpointResult

class SimpleEndpointCallWrapperChain(val wrappers: List<EndpointCallWrapper>) : EndpointCallWrapperChain {
    var currentIndex = 0

    override suspend fun chain(
        call: ApplicationCall,
        context: EndpointContext,
        block: suspend (call: ApplicationCall, context: EndpointContext) -> EndpointResult<*>
    ): EndpointResult<*> {
        return if (currentIndex < wrappers.size) {
            wrappers[currentIndex++].onCall(this, call, context, block)
        } else {
            block(call, context)
        }
    }
}
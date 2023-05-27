package it.zoo.vendro.pigeon.wrapping

import io.ktor.server.application.*
import it.zoo.vendro.pigeon.context.EndpointContext

interface EndpointCallWrapperChain {
    suspend fun chain(
        call: ApplicationCall,
        context: EndpointContext,
        block: suspend (call: ApplicationCall, context: EndpointContext) -> Unit
    )
}
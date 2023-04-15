package it.zoo.vendro.pigeon.endpoint

import io.ktor.server.application.*
import io.ktor.server.response.*
import it.zoo.vendro.pigeon.EndpointConfiguration
import it.zoo.vendro.pigeon.context.CommonKeys
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.endpoint.expansions.ContextShortcutExpansion
import it.zoo.vendro.pigeon.endpoint.expansions.ResultExpansion
import it.zoo.vendro.pigeon.exception.EndpointException
import it.zoo.vendro.pigeon.result.EndpointResult
import kotlin.reflect.KClass
import kotlin.reflect.KType

abstract class Endpoint<I, M, P, O>(
    val inputType: KType,
    val manipulatedType: KType,
    val processedType: KType,
    val outputType: KType,
    val endpointConfiguration: EndpointConfiguration = EndpointConfiguration.DEFAULT
        ?: throw IllegalArgumentException("EndpointConfiguration.DEFAULT is null, please set it before using the Endpoint class or pass a custom EndpointConfiguration to the Endpoint constructor")
) : ResultExpansion, ContextShortcutExpansion {
    final override var context: EndpointContext =
        EndpointContext().also {
            it[CommonKeys.CONNECTION_MANAGER] = null
        }
        private set

    init {
        inputType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("inputType must be a KClass")
        manipulatedType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("manipulatedType must be a KClass")
        processedType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("processedType must be a KClass")
        outputType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("outputType must be a KClass")
    }

    suspend fun transformWrapper(call: ApplicationCall, context: EndpointContext) = transform(call, context)

    abstract suspend fun transform(call: ApplicationCall, context: EndpointContext): I

    suspend fun manipulateWrapper(input: I, context: EndpointContext) = manipulate(input, context)

    abstract suspend fun manipulate(input: I, context: EndpointContext): M

    suspend fun call(call: ApplicationCall) {
        val input = transformWrapper(call, context)

        endpointConfiguration.connectionPool.getManager().use { manager ->
            writeResponse(call) {
                context[CommonKeys.CONNECTION_MANAGER] = manager
                val manipulated = manipulateWrapper(input, context)
                val processed = processWrapper(manipulated, context)
                respondWrapper(processed, context)
            }
        }
    }

    suspend fun processWrapper(manipulated: M, context: EndpointContext) = process(manipulated, context)

    abstract suspend fun process(manipulated: M, context: EndpointContext): EndpointResult<P>

    suspend fun respondWrapper(processed: EndpointResult<P>, context: EndpointContext) = respond(processed, context)

    abstract suspend fun respond(processed: EndpointResult<P>, context: EndpointContext): EndpointResult<O>

    suspend fun writeResponse(call: ApplicationCall, block: suspend () -> EndpointResult<O>) {
        try {
            val result = block()
            call.respond(result)
        } catch (e: EndpointException) {
            call.respond(e.toResult())
            throw e
        } catch (e: Exception) {
            call.respond(EndpointException(e.message, e).toResult())
            throw e
        }
    }

    companion object {
        inline fun <O> toResponse(block: () -> EndpointResult<O>): EndpointResult<O> {
            return try {
                block()
            } catch (e: EndpointException) {
                e.toTypedResult()
            } catch (e: Exception) {
                EndpointException(e.message, e).toTypedResult()
            }
        }
    }
}


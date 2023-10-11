package it.zoo.vendro.pigeon.endpoint

import io.ktor.server.application.*
import io.ktor.server.response.*
import it.zoo.vendro.pigeon.EndpointConfiguration
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.exception.EndpointException
import it.zoo.vendro.pigeon.result.EndpointResult
import it.zoo.vendro.pigeon.wrapping.SimpleEndpointCallWrapperChain
import kotlin.reflect.KClass
import kotlin.reflect.KType


/**
 * Represents an abstract class for an endpoint.
 *
 * @param I the input type for the endpoint (the raw type extracted from the request)
 * @param M the manipulated type for the endpoint (the type after the conversion to entity)
 * @param P the processed type for the endpoint (the type after the evaluation of the endpoint)
 * @param O the output type for the endpoint (the type after the conversion to response)
 * @param inputType the KType of the input type
 * @param manipulatedType the KType of the manipulated type
 * @param processedType the KType of the processed type
 * @param outputType the KType of the output type
 * @param endpointConfiguration the configuration for the endpoint
 *
 * @throws IllegalArgumentException if any of the type parameters are not of type KClass
 * @throws IllegalArgumentException if the default endpoint configuration is null
 */
abstract class Endpoint<I, M, P, O>(
    val inputType: KType,
    val manipulatedType: KType,
    val processedType: KType,
    val outputType: KType,
    val endpointConfiguration: EndpointConfiguration = EndpointConfiguration.DEFAULT
        ?: throw IllegalArgumentException("EndpointConfiguration.DEFAULT is null, please set it before using the Endpoint class or pass a custom EndpointConfiguration to the Endpoint constructor")
) {
    var context: EndpointContext =
        EndpointContext()
        internal set

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

    abstract suspend fun transform(call: ApplicationCall, context: EndpointContext): EndpointResult<I>

    suspend fun manipulateWrapper(input: EndpointResult<I>, context: EndpointContext) = manipulate(input, context)

    abstract suspend fun manipulate(input: EndpointResult<I>, context: EndpointContext): EndpointResult<M>

    suspend fun call(call: ApplicationCall) {
        SimpleEndpointCallWrapperChain(endpointConfiguration.wrappers)
            .chain(call, context) { innerCall, innerContext ->
                writeResponse(innerCall) {
                    endpointConfiguration.wrappers.forEach { it.onStart(innerCall, innerContext) }
                    val input = transformWrapper(innerCall, innerContext)
                    endpointConfiguration.wrappers.forEach { it.afterTransform(innerCall, innerContext) }
                    val manipulated = manipulateWrapper(input, innerContext)
                    endpointConfiguration.wrappers.forEach { it.afterManipulate(innerCall, innerContext) }
                    val processed = processWrapper(manipulated, innerContext)
                    endpointConfiguration.wrappers.forEach { it.afterProcess(innerCall, innerContext) }
                    val output = respondWrapper(processed, innerContext)
                    endpointConfiguration.wrappers.forEach { it.afterRespond(innerCall, innerContext, output) }
                    output
                }
            }
    }

    suspend fun processWrapper(manipulated: EndpointResult<M>, context: EndpointContext) = process(manipulated, context)

    abstract suspend fun process(manipulated: EndpointResult<M>, context: EndpointContext): EndpointResult<P>

    suspend fun respondWrapper(processed: EndpointResult<P>, context: EndpointContext) = respond(processed, context)

    abstract suspend fun respond(processed: EndpointResult<P>, context: EndpointContext): EndpointResult<O>

    suspend fun writeResponse(call: ApplicationCall, block: suspend () -> EndpointResult<O>): EndpointResult<O> {
        try {
            val result = block()
            call.respond(result.final())
            return result
        } catch (e: Exception) {
            handleException(e, call)
            throw e
        }
    }

    private suspend fun handleException(e: Exception, call: ApplicationCall) {
        if (e is EndpointException) {
            if (endpointConfiguration.writeResponseIfError) call.respond(e.toResult().final())
        } else {
            e.printStackTrace()
            if (endpointConfiguration.writeResponseIfError) call.respond(EndpointException(e.message, e).toResult().final())
        }
    }
}


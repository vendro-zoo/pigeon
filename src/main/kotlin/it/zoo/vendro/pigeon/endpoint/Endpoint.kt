package it.zoo.vendro.pigeon.endpoint

import habitat.RacoonDen
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.pigeon.declaration.RawInput
import it.zoo.vendro.pigeon.response.Response
import it.zoo.vendro.pigeon.response.ResponseIntegration
import kotlin.reflect.KClass

abstract class Endpoint<R : RawInput<I>, I : Any, O : Any>(
    private val kRawInput: KClass<R>,
) : ResponseIntegration {
    suspend fun call(call: ApplicationCall) {
        val rawInput = getRawInput(call)


        RacoonDen.getManager().use { rm ->
            val context = EndpointContext(rm)

            val input = getInput(rawInput, context)
            val result = internalExecute(input, context)

            if (!result.success) rm.rollback()
            else rm.commit()

            writeOutput(result, call)
        }
    }

    suspend fun getRawInput(call: ApplicationCall): R = call.receive(kRawInput)

    fun getInput(rawInput: R, context: EndpointContext): I = rawInput.parseRawInput(context)

    suspend fun writeOutput(output: Response<O>, call: ApplicationCall) = call.respond(output)

    abstract fun internalExecute(input: I, context: EndpointContext): Response<O>
}
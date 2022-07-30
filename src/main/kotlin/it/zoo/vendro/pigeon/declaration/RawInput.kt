package it.zoo.vendro.pigeon.declaration

import it.zoo.vendro.pigeon.context.EndpointContext

interface RawInput<I> {
    fun parseRawInput(context: EndpointContext): I
}

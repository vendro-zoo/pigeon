package it.zoo.vendro.pigeon.declaration

import it.zoo.vendro.pigeon.context.EndpointContext

interface SimpleRawInput<T : SimpleRawInput<T>> : RawInput<T> {
    override fun parseRawInput(context: EndpointContext): T = this as T
}
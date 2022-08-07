package it.zoo.vendro.pigeon.declaration

import it.zoo.vendro.pigeon.context.EndpointContext

interface SelfRawInput<T: SelfRawInput<T>> : RawInput<SelfRawInput<T>> {
    override fun parseRawInput(context: EndpointContext) = this
}
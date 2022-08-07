package it.zoo.vendro.pigeon.declaration

import it.zoo.vendro.pigeon.context.EndpointContext

class RawMap (
    val map: Map<String, Any?>
) : RawInput<Map<String, Any?>> {
    override fun parseRawInput(context: EndpointContext) = map
}
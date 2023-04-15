package it.zoo.vendro.pigeon.context

import kotlin.reflect.KType
import kotlin.reflect.typeOf

class EndpointContextKey<T>(val name: String, val type: KType) {
    companion object {
        inline operator fun <reified T> invoke(name: String) = EndpointContextKey<T>(name, typeOf<T>())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EndpointContextKey<*>

        if (name != other.name) return false
        return type == other.type
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }


}
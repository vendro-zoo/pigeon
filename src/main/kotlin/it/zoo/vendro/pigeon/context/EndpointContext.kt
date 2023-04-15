package it.zoo.vendro.pigeon.context

open class EndpointContext {
    val context = mutableMapOf<EndpointContextKey<*>, Any?>()

    operator fun <T> get(key: EndpointContextKey<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return if (!context.containsKey(key)) null
        else context[key] as T
    }

    operator fun <T> set(key: EndpointContextKey<T>, value: T) {
        context[key] = value
    }

    fun has(key: EndpointContextKey<*>): Boolean {
        return context.containsKey(key)
    }

    fun <T> getOrPut(key: EndpointContextKey<T>, defaultValue: () -> T): T =
        getOrPut(key, defaultValue())

    fun <T> getOrPut(key: EndpointContextKey<T>, defaultValue: T): T {
        if (has(key)) return get(key)!!
        return defaultValue.also { set(key, it) }
    }

    fun <T> remove(key: EndpointContextKey<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return context.remove(key) as T?
    }
}
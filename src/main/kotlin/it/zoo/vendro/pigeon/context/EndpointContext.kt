package it.zoo.vendro.pigeon.context

/**
 * Represents an endpoint context that stores key-value pairs.
 *
 * The context is mutable and can be used to store data that is shared between
 * different endpoints.
 */
open class EndpointContext {
    val context = mutableMapOf<EndpointContextKey<*>, Any?>()

    /**
     * Retrieves the value associated with the specified key from the context.
     *
     * @param key the key to retrieve the value for
     * @return the value associated with the key, or null if the key is not present
     */
    operator fun <T> get(key: EndpointContextKey<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return if (!context.containsKey(key)) null
        else context[key] as T
    }

    /**
     * Sets the value associated with the given key in the endpoint context.
     *
     * @param key the key to associate the value with
     * @param value the value to be associated with the key
     * @param T the type of value being set
     */
    operator fun <T> set(key: EndpointContextKey<T>, value: T) {
        context[key] = value
    }

    /**
     * Checks if the given key is present in the context.
     *
     * @param key The key to be checked within the context.
     * @return True if the key is present in the context, False otherwise.
     */
    fun has(key: EndpointContextKey<*>): Boolean {
        return context.containsKey(key)
    }

    /**
     * Retrieves the value associated with the specified key, or inserts and returns a default value
     * if the key is not found.
     *
     * @param key the key to retrieve or insert the value for
     * @param defaultValue a lambda function that provides the default value to be inserted if the key is not found
     * @return the value associated with the key, or the inserted default value if the key is not found
     */
    fun <T> getOrPut(key: EndpointContextKey<T>, defaultValue: () -> T): T =
        getOrPut(key, defaultValue())

    /**
     * Retrieves the value associated with the given key from the Endpoint Context, or puts the default value and returns it if the key does not exist.
     *
     * @param key The key associated with the value.
     * @param defaultValue The default value to put and return if the key does not exist.
     * @return The value associated with the key, if it exists. Otherwise, the defaultValue is put and returned.
     */
    fun <T> getOrPut(key: EndpointContextKey<T>, defaultValue: T): T {
        if (has(key)) return get(key)!!
        return defaultValue.also { set(key, it) }
    }

    /**
     * Removes the value associated with the specified key from the context.
     *
     * @param key the key of the value to be removed from the context
     * @return the value that was removed from the context, or null if the key was not found
     */
    fun <T> remove(key: EndpointContextKey<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return context.remove(key) as T?
    }
}
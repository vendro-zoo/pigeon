package it.zoo.vendro.pigeon.response

interface ResponseIntegration {
    fun <T> success(data: T? = null, message: String? = null) = Response.success(data, message)

    fun <T> error(message: String? = null, data: T? = null) = Response.error(message, data)
}
package it.zoo.vendro.pigeon.response

class Response<T> private constructor(
    val success: Boolean,
    val message: String?,
    val data: T?,
) {
    companion object {
        fun <T> success(data: T? = null, message: String? = null): Response<T> {
            return Response(true, message, data)
        }

        fun <T> error(message: String? = null, data: T? = null): Response<T> {
            return Response(false, message, data)
        }
    }
}
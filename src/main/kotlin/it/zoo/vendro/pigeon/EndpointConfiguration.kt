package it.zoo.vendro.pigeon

import it.zoo.vendro.pigeon.wrapping.EndpointCallWrapper

class EndpointConfiguration (
    val wrappers: List<EndpointCallWrapper>,

    /**
     * A flag indicating whether to write a response if an error occurs.
     * To be disabled when using StatusPages as it may cause a double response.
     *
     * @property writeResponseIfError true if response should be written in case of error, false otherwise.
     */
    val writeResponseIfError: Boolean = true
) {
    companion object {
        var DEFAULT: EndpointConfiguration? = null
            private set

        fun setDefaultConfiguration(configuration: EndpointConfiguration) {
            if (DEFAULT != null) throw IllegalStateException("Default configuration can be set only once")
            DEFAULT = configuration
        }
    }
}
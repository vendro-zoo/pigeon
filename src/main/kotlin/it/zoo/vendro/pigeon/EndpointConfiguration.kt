package it.zoo.vendro.pigeon

import it.zoo.vendro.pigeon.wrapping.EndpointCallWrapper

class EndpointConfiguration (
    val wrappers: List<EndpointCallWrapper>
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
package it.zoo.vendro.pigeon

import it.zoo.vendro.racoon.habitat.ConnectionPool

class EndpointConfiguration (
    val connectionPool: ConnectionPool
) {
    companion object {
        var DEFAULT: EndpointConfiguration? = null
            private set

        fun setDefaultConfiguration(configuration: EndpointConfiguration) {
            DEFAULT = configuration
        }
    }
}
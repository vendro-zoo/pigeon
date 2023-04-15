package it.zoo.vendro.pigeon.context

import it.zoo.vendro.racoon.habitat.ConnectionManager

object CommonKeys {
    val CONNECTION_MANAGER = EndpointContextKey<ConnectionManager?>("connectionManager")
}
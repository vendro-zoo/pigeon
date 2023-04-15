package it.zoo.vendro.pigeon.endpoint.expansions

import it.zoo.vendro.pigeon.context.CommonKeys
import it.zoo.vendro.pigeon.context.EndpointContext
import it.zoo.vendro.racoon.habitat.ConnectionManager

interface ContextShortcutExpansion {
    val context: EndpointContext
    val cm: ConnectionManager
        get() = context[CommonKeys.CONNECTION_MANAGER]
            ?: throw IllegalStateException("ConnectionManager not found in context")
}

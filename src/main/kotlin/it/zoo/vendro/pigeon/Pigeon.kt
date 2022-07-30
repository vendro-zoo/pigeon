package it.zoo.vendro.pigeon

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.util.*
import java.lang.Exception
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object Pigeon {
    suspend fun handleException(e: Exception, call: ApplicationCall) {
        exceptions[e::class]?.invoke(call, e)
    }

    val exceptions: MutableMap<KClass<*>, suspend (call: ApplicationCall, cause: Throwable) -> Unit> = mutableMapOf()
}

fun Application.configurePigeon() {
    @Suppress("UNCHECKED_CAST")
    Pigeon.exceptions.putAll((PluginInstance::class.memberProperties.first()
        .get(this.pluginRegistry[AttributeKey("StatusPages")] as PluginInstance)
            as PluginBuilder<StatusPagesConfig>).pluginConfig.exceptions)
}
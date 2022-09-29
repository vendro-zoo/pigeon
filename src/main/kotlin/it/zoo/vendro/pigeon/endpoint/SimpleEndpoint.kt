package it.zoo.vendro.pigeon.endpoint

import it.zoo.vendro.pigeon.declaration.RawInput
import kotlin.reflect.KClass

abstract class SimpleEndpoint<I: RawInput<I>, O: Any>(kRawInput: KClass<I>) : Endpoint<I, I, O>(kRawInput)
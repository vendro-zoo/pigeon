@file:OptIn(ExperimentalContracts::class)

package it.zoo.vendro.pigeon.result

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.recover
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

typealias EndpointResult<T> = TypedEndpointResult<Any?, T>

sealed class TypedEndpointResult<out E, out V> {
    data class Ok<out V>(val value: V) : TypedEndpointResult<Nothing, V>() {
        override fun final(): FinalEndpointResult<*> = FinalEndpointResult.of(this)
    }

    data class Error<out E>(val error: E) : TypedEndpointResult<E, Nothing>() {
        override fun final(): FinalEndpointResult<*> = FinalEndpointResult.of(this)
    }

    abstract fun final(): FinalEndpointResult<*>
}

inline fun <E, V, N> TypedEndpointResult<E, V>.flatMap(f: (value: V) -> TypedEndpointResult<E, N>): TypedEndpointResult<E, N> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is TypedEndpointResult.Ok -> f(value)
        is TypedEndpointResult.Error -> this
    }
}

inline fun <E, V, N> TypedEndpointResult<E, V>.flatMapError(f: (error: E) -> TypedEndpointResult<N, V>): TypedEndpointResult<N, V> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is TypedEndpointResult.Ok -> this
        is TypedEndpointResult.Error -> f(error)
    }
}

inline fun <E, V, N> TypedEndpointResult<E, V>.map(f: (value: V) -> N): TypedEndpointResult<E, N> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return flatMap { TypedEndpointResult.Ok(f(it)) }
}

inline fun <E, V, N> TypedEndpointResult<E, V>.mapError(f: (error: E) -> N): TypedEndpointResult<N, V> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return flatMapError { TypedEndpointResult.Error(f(it)) }
}

inline fun <E, V, N> TypedEndpointResult<E, V>.isOk(predicate: (V) -> Boolean = { true }): Boolean {
    contract {
        returns(true) implies (this@isOk is TypedEndpointResult.Ok)
        callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
    }
    return this is TypedEndpointResult.Ok<V> && predicate(value)
}

inline fun <E, V, N> TypedEndpointResult<E, V>.isError(predicate: (E) -> Boolean = { true }): Boolean {
    contract {
        returns(true) implies (this@isError is TypedEndpointResult.Error)
        callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
    }
    return this is TypedEndpointResult.Error<E> && predicate(error)
}

data class FinalEndpointResult<V>(val status: EndpointResultStatus, val value: V) {
    companion object {
        fun <V> of(ok: TypedEndpointResult.Ok<V>): FinalEndpointResult<V> =
            FinalEndpointResult(EndpointResultStatus.OK, ok.value)

        fun <V> of(ok: TypedEndpointResult.Error<V>): FinalEndpointResult<V> =
            FinalEndpointResult(EndpointResultStatus.ERROR, ok.error)

        fun <E, V> of(result: TypedEndpointResult<E, V>): FinalEndpointResult<*> = when (result) {
            is TypedEndpointResult.Ok -> of(result)
            is TypedEndpointResult.Error -> of(result)
        }
    }
}

@JvmInline
value class EndpointResultRaise<E>(val raise: Raise<TypedEndpointResult<E, Nothing>>) :
    Raise<TypedEndpointResult<E, Nothing>> by raise {
    fun <A> TypedEndpointResult<E, A>.bind(): A = when (this) {
        is TypedEndpointResult.Ok -> value
        is TypedEndpointResult.Error -> raise.raise(this)
    }

    fun <A> Either<E, A>.bindEither(): A = when (this) {
        is Either.Left -> raise.raise(TypedEndpointResult.Error(value))
        is Either.Right -> value
    }

    fun error(error: E): Nothing = raise(TypedEndpointResult.Error(error))
}

@OptIn(ExperimentalTypeInference::class)
inline fun <E, A> endpointResult(@BuilderInference block: EndpointResultRaise<E>.() -> A): TypedEndpointResult<E, A> =
    recover({ TypedEndpointResult.Ok(block(EndpointResultRaise(this))) }) { e: TypedEndpointResult<E, Nothing> -> e }
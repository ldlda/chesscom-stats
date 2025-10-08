package com.ldlda.chesscom_stats.util

/*
 * ldaCheck { it().getOrThrow()} {what()}
 */

typealias Strategy<T, R> = (() -> Result<T>) -> R
typealias StratEval<T, R> = (() -> T) -> R
typealias StratEvalExtension<A, T, R> = (A.() -> T) -> R

/**
 * This is a function that goes as follows.
 * ```kotlin
 * val c: C = ldaCheck { strategy for that --> } { this returns B }
 * ```
 *
 * @param strategy this lambda will take a () -> Result<B>.
 *
 * you may call it() amd work with this result or you dont.
 *
 * this lambda returns C
 *
 * @return a function that takes nothing and returns B. lowk this is [run][Any.run] written in reverse
 */
inline fun <B, C> ldaCheck(crossinline strategy: Strategy<B, C>): StratEval<B, C> =
    { strategy { runCatching(it) } }

/**
 * refer to [ldaCheck][ldaCheck as Function1<*, *>]
 *
 * ```kotlin
 * val a: A = lowk ass
 * val c: C = a.ldaCheck { it().getOrThrow() } { cuh() } // prolly runs a.cuh
 * ```
 *
 * @receiver any; will be passed in the second call as this
 */
inline fun <A, B, C> A.ldaCheck(crossinline strategy: Strategy<B, C>): StratEvalExtension<A, B, C> =
    { strategy { runCatching { this.it() } } }


fun <B> ldaCheckThis(check: Boolean, strict: Boolean, block: () -> B): B? {
    val that: Strategy<B, B?> = check bih strict

    return ldaCheck(that)(block)
}

fun <A, B> A.ldaCheckThis(check: Boolean, strict: Boolean, block: A.() -> B): B? {
    val that: Strategy<B, B?> = check bih strict

    return this.ldaCheck(that)(block)
}

private infix fun <B> Boolean.bih(that: Boolean): Strategy<B, B?> =
    when (this to that) {
        (true to false) -> {
            { null }
        }

        (true to true) -> {
            @Throws(Exception::class)
            { it().getOrThrow() }
        }

        else -> {
            { it().getOrNull() }
        }
    }


inline fun requireNot(ifTrue: Boolean, raiseMsg: () -> Any) = require(!ifTrue, raiseMsg)

fun requireNot(ifTrue: Boolean) = require(!ifTrue)
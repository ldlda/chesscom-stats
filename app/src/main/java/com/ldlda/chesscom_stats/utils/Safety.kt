package com.ldlda.chesscom_stats.utils

/*
 * ldaCheck { it().getOrThrow()} {what()}
 */

typealias Strategy<T, R> = (() -> Result<T>) -> R
typealias StratEval<T, R> = (() -> T) -> R

inline fun <B, C> ldaCheck(crossinline strategy: Strategy<B, C>): StratEval<B, C> =
    { strategy { runCatching(it) } }

fun <B, C: Any?> ldaCheckThis(check: Boolean, strict: Boolean, block: () -> B): C {
    val a = when (check to strict) {
        (true to false) -> {
            println("runNull")
            runNull
        }

        (true to true) -> {
            println("runThrow")
            runThrow
        }

        else -> {
            println("nothing")
            nothing
        }
    }
    @Suppress("unchecked_cast")
    val that = a as Strategy<B, C>
    return ldaCheck(that)(block)
}

val nothing: Strategy<*, *> = { null }

val runThrow: Strategy<*, *> =
    @Throws(Exception::class)
    { it().getOrThrow() }
val runNull: Strategy<*, *> = { it().getOrNull() }


inline fun requireNot(ifTrue: Boolean, raiseMsg: () -> Any) = require(!ifTrue, raiseMsg)

fun requireNot(ifTrue: Boolean) = require(!ifTrue)
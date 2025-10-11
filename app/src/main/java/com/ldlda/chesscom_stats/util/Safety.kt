package com.ldlda.chesscom_stats.util

/*
 * ldaCheck { it().getOrThrow()} {what()}
 */

// lowk this stays the same
typealias Strategy<T, R> = (StrategyArg<T>) -> R
typealias StrategyArg<T> = () -> Result<T>

typealias StratEval<T, R> = (StratEvalArg<T>) -> R
typealias StratEvalArg<T> = () -> T

typealias StratEvalExtension<E, T, R> = (StratEvalExtensionArg<E, T>) -> R
typealias StratEvalExtensionArg<E, T> = (E.() -> T)

// typealias StrategyArgWithArguments<A, T> = (A) -> Result<T>
// typealias StratEvalExtensionArgWithArguments<E, A, T> = (E.(A) -> T)
// you can supply args here even but i cant bro what the fuck
// ts a whole class of type bro
// lowk tedious

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

inline fun <B, C> ldaCheck(
    crossinline strategy: Strategy<B, C>,
    crossinline toEval: StratEvalArg<B>
): C =
    strategy { runCatching(toEval) }

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
    { strategy { runCatching(it) } }

inline fun <A, B, C> A.ldaCheck(
    crossinline strategy: Strategy<B, C>,
    crossinline toEval: StratEvalExtensionArg<A, B>
): C =
    strategy { runCatching(toEval) }

fun <B> ldaCheckThis(check: Boolean, strict: Boolean, block: StratEvalArg<B>): B? {
    val that: Strategy<B, B?> = bih(check, strict)

    return ldaCheck(that, block)
}

fun <A, B> A.ldaCheckThis(check: Boolean, strict: Boolean, block: StratEvalExtensionArg<A, B>): B? {
    val that: Strategy<B, B?> = bih(check, strict)

    return this.ldaCheck(that, block)
}

fun <B> ldaCheckThis(check: Boolean, strict: Boolean): StratEval<B, B?> {
    val that: Strategy<B, B?> = bih(check, strict)

    return ldaCheck(that)
}

fun <A, B> A.ldaCheckThis(check: Boolean, strict: Boolean): StratEvalExtension<A, B, B?> {
    val that: Strategy<B, B?> = bih(check, strict)
    return this.ldaCheck(that)
}

private fun <B> bih(a: Boolean, b: Boolean): Strategy<B, B?> =
    when (a to b) {
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

infix fun Boolean.requiredOr(e: () -> Any) = require(this, e)
infix fun Boolean.requiredNotOr(e: () -> Any) = requireNot(this, e)
infix fun <T> T?.requiredNotNullOr(e: () -> Any): T = requireNotNull(this, e)

@Deprecated("no")
class LdaOn<T, U, V>(override val carriedThis: T, override val strategy: Strategy<U, V>) :
    LdaOnTrait<T, U, V>

@Deprecated("no")
interface LdaOnTrait<T, U, V> {
    val carriedThis: T
    val strategy: Strategy<U, V>

    @Deprecated("no", ReplaceWith("this(toBeEval)"))
    infix fun on(toBeEval: StratEvalExtensionArg<T, U>): V =
        carriedThis.ldaCheck(strategy, toBeEval)

    @Deprecated("no")
    operator fun invoke(toBeEval: StratEvalExtensionArg<T, U>): V = on(toBeEval)
}

/**
 * this lowk the biggest fuck you to the type system
 *
 * call:
 * ```
 * objectT.ldaRun {
 *  // this takes () -> Result<U> and returns V
 *  // what the fuck is () -> Result<U>
 *      it().getOrNull()?.toV()
 *  // in this case this returns V? the type
 * } on {
 *   this.TFunctions() // this is (hopefully) objectT
 *   // this returns U. the above got a { runCatching(this lambda) }
 * }
 * ```
 *
 * # why
 *
 * why not run
 * ```
 * strategy { this.runCatching(toBeEval) }
 * ```
 * directly? idk
 *
 * @param strategy i call it strategy but its just applying
the fn that you supply to [on][LdaOnTrait.on]
 * @return a thing of [LdaOnTrait] that you run [on][LdaOnTrait.on] on
 */
@Deprecated("no", ReplaceWith("ldaCheck(strategy)"))
fun <T, U, V> T.ldaRun(strategy: Strategy<U, V>): LdaOn<T, U, V> = LdaOn(this, strategy)

val checkFn = { check: Boolean -> ldaCheckThis<Unit>(check, true) }

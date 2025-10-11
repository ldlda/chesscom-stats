package com.ldlda.chesscom_stats.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.future
import kotlin.coroutines.CoroutineContext

/**
 * Utilities for bridging suspend -> CompletableFuture for Java callers without
 * forcing the rest of the stack to abandon coroutines.
 *
 * Design choice: we keep this minimal and eager. A 'lazy' CompletableFuture
 * implementation is possible (and briefly existed) but is non-idiomatic in
 * typical CF usage where creation time == start time. If you need laziness,
 * prefer wrapping a Supplier or using suspend functions directly.
 */
object Futures {
    /**
     * Immediately launches the supplied suspend [block] on [context]
     * and returns a cancellable [java.util.concurrent.CompletableFuture].
     */
    @JvmStatic
    @JvmOverloads
    fun <T> eager(
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ) = CoroutineScope(SupervisorJob() + context).future { block() }

}
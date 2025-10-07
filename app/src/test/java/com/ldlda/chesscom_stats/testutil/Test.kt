package com.ldlda.chesscom_stats.testutil

fun String.real() = "real af: $this"
// java invocation with TestKt.real(str) AHHHH why cant i do the reverse

fun <T> goodThing(real: () -> T) = object : Real<T> {
    override fun y() = real()
    override fun n() = try {
        real()
    } catch (e: IllegalArgumentException) {
        null
    } catch (e: IllegalStateException) {
        null
    }
}

interface Real<T> {
    fun y(): T;
    fun n(): T?

    fun d(y: Boolean): T? = if (y) y() else n()
}

abstract class Cool<T, U>(val block: () -> T) {
    abstract fun check(): U;
}

class Raise<T>(block: () -> T) : Cool<T, T>(block) {
    override fun check(): T = block()
}

class Null<T>(block: () -> T) : Cool<T, T?>(block) {
    override fun check(): T? = try {
        block()
    } catch (_: Exception) {
        null
    }
}

class Result<T>(block: () -> T) : Cool<T, kotlin.Result<T>>(block) {
    override fun check(): kotlin.Result<T> = try {
        kotlin.Result.success(block())
    } catch (e: Exception) {
        kotlin.Result.failure(e)
    }
}

class Println<T>(block: () -> T) : Cool<T, Unit>(block) {
    override fun check() = try {
        println("Success: ${block()}")
    } catch (e: Exception) {
        println("Failure: $e")
    }
}


fun <T> dispatch(lowkATable: Damn, block: () -> T) =
    when (lowkATable) {
        Damn.Raise -> Raise(block)
        Damn.Null -> Null(block)
        Damn.Result -> Result(block)
        Damn.PrintlnAhh -> Println(block)
    }

enum class Damn {
    Raise,
    Null,
    Result,
    PrintlnAhh
}
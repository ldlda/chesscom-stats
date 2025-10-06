package com.ldlda.chesscom_stats

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun works() {
        Instant.parse("2023-02-13T19:49:42Z")
    }
}
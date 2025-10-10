package com.ldlda.chesscom_stats

import com.ldlda.chesscom_stats.api.data.CountryInfo.Companion.extractCountryCodeFromUrl
import com.ldlda.chesscom_stats.api.data.playergames.MonthlyArchives
import com.ldlda.chesscom_stats.api.data.playergames.MonthlyArchives.Companion.mapMonthlyArchivesDetail
import com.ldlda.chesscom_stats.api.data.playerstats.Game
import com.ldlda.chesscom_stats.testutil.Damn
import com.ldlda.chesscom_stats.testutil.dispatch
import com.ldlda.chesscom_stats.testutil.goodThing
import com.ldlda.chesscom_stats.testutil.real
import com.ldlda.chesscom_stats.util.StratEvalExtensionArg
import com.ldlda.chesscom_stats.util.Strategy
import com.ldlda.chesscom_stats.util.ldaCheck
import com.ldlda.chesscom_stats.util.ldaCheckThis
import com.ldlda.chesscom_stats.util.ldaRun
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import kotlin.math.pow

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

    @Test
    fun so_works() {
        val fuh = "https://api.chess.com/pub/"
        val cuhwuh = "https://api.chess.com/pub/country/US/"
        assertEquals("US", extractCountryCodeFromUrl(fuh, cuhwuh))
        val wijgij = "https://api.chess.com/pub/player/erik/games/2007/07"
        assertEquals(
            MonthlyArchives.Detail("erik", "2007", "07"),
            wijgij.toHttpUrl().mapMonthlyArchivesDetail(fuh.toHttpUrl(), true)
        )
    }

    @Test
    fun maybe() {
        assertTrue("real".real() == "real af: real")
        val baby = { throw IllegalArgumentException("nothing lowk") }
        val what = goodThing(baby)
        try {
            what.y()
            // fail("you fuhhed up son") // kotlin is smart
        } catch (e: IllegalArgumentException) {
            assertEquals("nothing lowk", e.message)
        }

        val cuh = dispatch(Damn.Result, baby)

        val real = cuh.check() // what the FUCK is Any?
        println(real)

        println(ldaCheckThis<Any, Any>(check = true, strict = false) {})
    }

    @Test
    fun not_so_much() {
        val lda = "https://ldlda.com/blogs/"
        val uh = listOf("blogs", "")
        assertEquals(uh, lda.toHttpUrl().encodedPathSegments) // WHAT THE FUCK

        val lda2 = "https://ldlda.com/blogs/minecraft"
        val uh2 = listOf("blogs", "minecraft")

        assertEquals(uh2, lda2.toHttpUrl().encodedPathSegments) // wym
    }

    val jsonIUK = Json { ignoreUnknownKeys = true }

    @Test
    fun lowk_ass() {
        val low = jsonIUK.decodeFromString<Game>(
            """
         {
          "win": 3062,
          "loss": 1975,
          "draw": 369,
          "time_per_move": 12690,
          "timeout_percent": 0
          }   
        """.trimIndent()
        ).also(::println)
        assertEquals(3062, low.win)
    }

    @Test
    fun out_of_everything() {
        val a: Strategy<Double, Double> = { t ->
            val low = t()
            low.getOrElse {
                3.1
            }
        }
        val b: StratEvalExtensionArg<String, Double> = {
            this.toDouble()
        }
        val threePointOne: Double = "lowk".ldaRun(a) on b
        val also3p1: Double = "lowk".ldaCheck(a)(b)
        val twopointeight = "2.8".ldaCheck(a, b)
        val toosmall = 10.0.pow(-15)
        assertEquals(3.1, threePointOne, toosmall)
        assertEquals(3.1, also3p1, toosmall)
        assertEquals(2.8, twopointeight, toosmall)
    }
}
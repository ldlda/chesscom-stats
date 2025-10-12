package com.ldlda.chesscom_stats.api.fetch

import androidx.annotation.WorkerThread
import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchRequest
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.api.repository.JavaChessRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.CompletableFuture

/*
    what is this bullshit
 */
@Deprecated("Use ChessRepositoryImpl or its subclasses")
class ChessApiClient : ChessRepository, JavaChessRepository {
    companion object {
        @Throws(ChessApiException::class)
        suspend fun <T, U> U.callApi(get: suspend (U) -> T): T {
            try {
                return get(this)
            } catch (e: Exception) {
                throw ChessApiException.build(e)
            }
        }
        const val CHESS_API_URL = "https://api.chess.com/pub/"
        val defaultOkHttp by lazy {
            OkHttpClient.Builder()
                //  TODO: This are not enabled for now, enable when ETag caching is fleshed out
//            .addInterceptor(AddIfNoneMatchInterceptor())
//            .addNetworkInterceptor(CaptureEtagAndServe304FromCacheInterceptor())
                .build()
        }

        private fun OkHttpClient.buildRetrofit(baseUrl: String): Retrofit {
            val okHttp = this
            val json = Json {
                ignoreUnknownKeys = true
//                encodeDefaults = true
                /* this or [ChessSearchRequest] has to have every keys serialized */
            }
            val contentType = "application/json".toMediaType()
            return Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttp)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        val defaultRetrofit: Retrofit by lazy {
            defaultOkHttp.buildRetrofit(CHESS_API_URL)
        }


        val defaultPublicService: ChessApiService by lazy {
            defaultRetrofit.create()
        }
        val defaultInstance = ChessApiClient()
    }

    val baseUrl: String
    val publicRetrofit: Retrofit
    val privateRetrofit: Retrofit

    val publicService: ChessApiService by lazy { publicRetrofit.create() }

    val privateService: PrivateApiService by lazy { publicRetrofit.create() }

    @JvmOverloads
    constructor(
        baseUrl: String = CHESS_API_URL,
        okHttp: OkHttpClient = defaultOkHttp,
    ) {
        this.baseUrl = baseUrl
        publicRetrofit = okHttp.buildRetrofit(baseUrl)
        privateRetrofit = publicRetrofit
    }

    constructor(
        retrofit: Retrofit = defaultRetrofit
    ) {
        this.publicRetrofit = retrofit
        baseUrl = retrofit.baseUrl().toString()
        privateRetrofit = publicRetrofit
    }

    constructor(
        publicRetrofit: Retrofit,
        privateRetrofit: Retrofit
    ) {
        this.publicRetrofit = publicRetrofit
        this.privateRetrofit = privateRetrofit
        baseUrl = publicRetrofit.baseUrl().toString()
    }



    // kotlin interface
    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getLeaderboards(): Leaderboards =
        publicService.callApi { it.leaderboards() }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getPlayer(username: String): Player =
        publicService.callApi { it.player(username) }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getPlayerStats(username: String): PlayerStats =
        publicService.callApi { it.playerStats(username) }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getCountry(code: String): CountryInfo =
        publicService.callApi { it.country(code) }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getCountry(countryUrl: HttpUrl): CountryInfo =
        publicService.callApi { it.country(countryUrl) }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun searchPlayers(prefix: String): List<SearchItem> =
        privateService.callApi { it.autocompleteUsername(SearchRequest(prefix)).suggestions }

    override suspend fun searchPlayers(request: SearchRequest): List<SearchItem> =
        privateService.callApi { it.autocompleteUsername(request).suggestions }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getMonthlyArchivesList(username: String): List<HttpUrl> =
        publicService.callApi { it.monthlyArchivesList(username).archives }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getMonthlyArchives(
        username: String,
        year: Int,
        month: Int
    ): List<MonthlyGame> {
        require(year > 0)
        require(month > 0 && month <= 12)
        return publicService.callApi {
            it.monthlyArchives(
                username,
                "%04d".format(year),
                "%02d".format(month),
            ).games
        }
    }

    @Deprecated("use ChessRepositoryImpl")
    override suspend fun getMonthlyArchives(url: HttpUrl) =
        publicService.callApi { it.monthlyArchives(url).games }

    override suspend fun getCountryClubs(code: String): List<HttpUrl> {
        TODO("Not yet implemented")
    }

    override suspend fun getClub(url: HttpUrl): Club {
        TODO("Not yet implemented")
    }

    override suspend fun getClub(nameId: String): Club {
        TODO("Not yet implemented")
    }

    // deprecated
    @Deprecated("use ChessRepository instead")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Deprecated("use ChessRepository instead")
    fun close() = scope.cancel()

    @Deprecated("use ChessRepository instead")
    // ok
    private fun <T> getAsync(get: suspend CoroutineScope.() -> T): CompletableFuture<T> =
        scope.future(block = get)

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    private fun <T> getSync(get: suspend CoroutineScope.() -> T): T =
        runBlocking(scope.coroutineContext, get)

    // Public synchronous functions for Java

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerSync(username: String): Player = getSync { getPlayer(username) }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getPlayerStatsSync(username: String): PlayerStats = getSync { getPlayerStats(username) }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getLeaderboardsSync(): Leaderboards = getSync { getLeaderboards() }

    @Deprecated("use ChessRepository instead")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountryByUrlSync(url: HttpUrl): CountryInfo = getSync { getCountry(url) }

    @Deprecated("use ChessRepository instead. Direct country fetching by code is discouraged.")
    @WorkerThread
    @Throws(ChessApiException::class)
    fun getCountrySync(code: String): CountryInfo = getSync { getCountry(code) }


    // Java-friendly async wrappers (recommended for UI)

    @Deprecated("use ChessRepository instead")
    override fun getPlayerAsync(username: String): CompletableFuture<Player> =
        getAsync { getPlayer(username) }

    @Deprecated("use ChessRepository instead")
    override fun getPlayerStatsAsync(username: String): CompletableFuture<PlayerStats> =
        getAsync { getPlayerStats(username) }

    @Deprecated("use ChessRepository instead")
    override fun getLeaderboardsAsync(): CompletableFuture<Leaderboards> =
        getAsync { getLeaderboards() }

    @Deprecated("use ChessRepository instead")
    override fun getCountryByUrlAsync(url: HttpUrl): CompletableFuture<CountryInfo> =
        getAsync { getCountry(url) }

    @Deprecated("use ChessRepository instead")
    @Throws(ChessApiException::class)
    override fun searchPlayersAsync(username: String): CompletableFuture<List<SearchItem>> =
        getAsync { searchPlayers(username) }

    override fun getClubAsync(nameId: String): CompletableFuture<Club> {
        TODO("Not yet implemented")
    }

    override fun getClubAsync(clubUrl: HttpUrl): CompletableFuture<Club> {
        TODO("Not yet implemented")
    }

    override fun getCountryClubsAsync(code: String): CompletableFuture<List<HttpUrl>> {
        TODO("Not yet implemented")
    }
}
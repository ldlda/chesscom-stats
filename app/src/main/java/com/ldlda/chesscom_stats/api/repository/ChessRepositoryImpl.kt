package com.ldlda.chesscom_stats.api.repository

import com.ldlda.chesscom_stats.api.data.club.Club
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.games.monthly.MonthlyGame
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchRequest
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient.Companion.callApi
import com.ldlda.chesscom_stats.api.fetch.ChessApiService
import com.ldlda.chesscom_stats.api.fetch.PrivateApiService
import okhttp3.HttpUrl

/**
 * Basic implementation of [ChessRepository] that delegates all API calls to the provided [ChessApiClient].
 * This implementation does not perform any caching or additional processing.
 *
 * Also implements [JavaChessRepository] by lazily delegating to [ChessRepoAdapterJava] to avoid circular dependency.
 */
open class ChessRepositoryImpl(
    val publicService: ChessApiService,
    val privateService: PrivateApiService,
) : ChessRepository {
    constructor(
        chessApiClient: ChessApiClient
    ) : this(chessApiClient.publicService, chessApiClient.privateService)

    // ChessRepository implementation (suspend methods)

    override suspend fun getLeaderboards(): Leaderboards =
        publicService.callApi { it.leaderboards() }

    override suspend fun getPlayer(username: String): Player =
        publicService.callApi { it.player(username) }

    override suspend fun getPlayerStats(username: String): PlayerStats =
        publicService.callApi { it.playerStats(username) }

    override suspend fun getCountry(code: String): CountryInfo =
        publicService.callApi { it.country(code) }

    override suspend fun getCountry(countryUrl: HttpUrl): CountryInfo =
        publicService.callApi { it.country(countryUrl) }

    override suspend fun searchPlayers(prefix: String): List<SearchItem> =
        privateService.callApi { it.autocompleteUsername(SearchRequest(prefix)) }.suggestions

    override suspend fun searchPlayers(request: SearchRequest): List<SearchItem> =
        privateService.callApi { it.autocompleteUsername(request) }.suggestions

    override suspend fun getMonthlyArchivesList(username: String): List<HttpUrl> =
        publicService.callApi { it.monthlyArchivesList(username) }.archives

    override suspend fun getMonthlyArchives(
        username: String, year: Int, month: Int
    ): List<MonthlyGame> {
        require(year > 0)
        require(month > 0 && month <= 12)
        val yearFmt = "%04d".format(year)
        val monthFmt = "%02d".format(month)
        return publicService.callApi {
            it.monthlyArchives(
                username,
                yearFmt,
                monthFmt,
            )
        }.games
    }

    override suspend fun getMonthlyArchives(url: HttpUrl) =
        publicService.callApi { it.monthlyArchives(url) }.games

    override suspend fun getCountryClubs(code: String): List<HttpUrl> =
        publicService.callApi { it.countryClubs(code) }.clubURLs


    override suspend fun getClub(url: HttpUrl): Club =
        publicService.callApi { it.club(url) }

    override suspend fun getClub(nameId: String): Club =
        publicService.callApi { it.club(nameId) }

}
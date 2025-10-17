package com.ldlda.chesscom_stats.ui.playerdetail

import android.os.Parcelable
import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.Title
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.data.timeclass.Rule
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import com.ldlda.chesscom_stats.ui.favorites.UserFavoriteModel
import com.ldlda.chesscom_stats.util.parcelize.httpurl.HttpUrlParceler
import com.ldlda.chesscom_stats.util.parcelize.httpurl.HttpUrlParcelerNullable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.WriteWith
import okhttp3.HttpUrl
import java.time.Instant

/**
 * Everything the PlayerDetailActivity needs to render.
 *
 * All fields nullable - progressively fill in as data arrives.
 *
 * Merge partial data from different sources (SearchItem, LeaderboardEntry, Player API, etc.)
 *
 * Parcelable - can be passed through Intent bundles.
 */
@Parcelize
@TypeParceler<HttpUrl, HttpUrlParceler>()
@JvmRecord
data class PlayerDetailData(
    val username: String? = null,
    val playerId: Long? = null,
    val avatar: @WriteWith<HttpUrlParcelerNullable> HttpUrl?,
    val name: String? = null,
    val title: Title? = null,
    val country: CountryInfo? = null,
    val followers: Int? = null,
    val fide: Int? = null,
    val joined: Instant? = null,
    val lastOnline: Instant? = null,
    val profileUrl: @WriteWith<HttpUrlParcelerNullable> HttpUrl? = null,

    // Basic ratings (current only)
    val bulletRating: Int? = null,
    val blitzRating: Int? = null,
    val rapidRating: Int? = null,
    val dailyRating: Int? = null,

    // Detailed stats for each timeclass (best rating + W/L/D)
    val bulletBest: Int? = null,
    val bulletWins: Int? = null,
    val bulletLosses: Int? = null,
    val bulletDraws: Int? = null,

    val blitzBest: Int? = null,
    val blitzWins: Int? = null,
    val blitzLosses: Int? = null,
    val blitzDraws: Int? = null,

    val rapidBest: Int? = null,
    val rapidWins: Int? = null,
    val rapidLosses: Int? = null,
    val rapidDraws: Int? = null,

    val dailyBest: Int? = null,
    val dailyWins: Int? = null,
    val dailyLosses: Int? = null,
    val dailyDraws: Int? = null,

    // Leaderboard metadata (if came from leaderboard)
    val leaderboardRank: Int? = null,
    val leaderboardTimeclass: String? = null, // "bullet", "blitz", "rapid", "daily"

    // Favorites metadata (if favorited)
    val favoriteSince: Instant? = null,
) : Parcelable {
    // Minimal constructor - just username
    constructor(username: String?) : this(
        username,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        null, null, null, null,
        null, null,
        null
    )

    /**
     * Merge this data with another PlayerDetailData.
     *
     * Prefers non-null values from either source (`this` takes priority if both non-null).
     *
     * CAUTION: THIS DOES NOT CARE IF you merge two of different people.
     */
    fun mergeWith(other: PlayerDetailData): PlayerDetailData {
        return PlayerDetailData(
            this.username ?: other.username,
            this.playerId ?: other.playerId,
            this.avatar ?: other.avatar,
            this.name ?: other.name,
            this.title ?: other.title,
            this.country ?: other.country,
            this.followers ?: other.followers,
            this.fide ?: other.fide,
            this.joined ?: other.joined,
            this.lastOnline ?: other.lastOnline,
            this.profileUrl ?: other.profileUrl,

            this.bulletRating ?: other.bulletRating,
            this.blitzRating ?: other.blitzRating,
            this.rapidRating ?: other.rapidRating,
            this.dailyRating ?: other.dailyRating,

            this.bulletBest ?: other.bulletBest,
            this.bulletWins ?: other.bulletWins,
            this.bulletLosses ?: other.bulletLosses,
            this.bulletDraws ?: other.bulletDraws,

            this.blitzBest ?: other.blitzBest,
            this.blitzWins ?: other.blitzWins,
            this.blitzLosses ?: other.blitzLosses,
            this.blitzDraws ?: other.blitzDraws,

            this.rapidBest ?: other.rapidBest,
            this.rapidWins ?: other.rapidWins,
            this.rapidLosses ?: other.rapidLosses,
            this.rapidDraws ?: other.rapidDraws,

            this.dailyBest ?: other.dailyBest,
            this.dailyWins ?: other.dailyWins,
            this.dailyLosses ?: other.dailyLosses,
            this.dailyDraws ?: other.dailyDraws,

            this.leaderboardRank ?: other.leaderboardRank,
            this.leaderboardTimeclass ?: other.leaderboardTimeclass,

            this.favoriteSince ?: other.favoriteSince
        )
    }

    // Check what's missing for smart fetching
    fun requiresPlayer(): Boolean {
        return playerId == null || avatar == null || name == null || title == null || followers == null || joined == null || lastOnline == null || profileUrl == null
    }

    fun requiresStats(): Boolean {
        return fide == null || bulletRating == null || blitzRating == null || rapidRating == null || dailyRating == null
    }

    fun requiresCountry(): Boolean {
        return country == null
    }

    fun requiresFavorite(): Boolean {
        return favoriteSince == null
    }

    companion object {
        // ========== Factory Methods ==========
        @JvmStatic
        fun fromSearchItem(item: SearchItem): PlayerDetailData {
            val user = item.userView

            // Extract ratings from SearchItem
            var bullet: Int? = null
            var blitz: Int? = null
            var rapid: Int? = null
            var daily: Int? = null
            for (rating in item.ratings) {
                if (rating.variantClass != Rule.Chess) continue
                when (rating.variantTime) {
                    Time.Bullet -> bullet = rating.rating
                    Time.Blitz -> blitz = rating.rating
                    Time.Rapid -> rapid = rating.rating
                    Time.Daily -> daily = rating.rating
                }
            }

            // Build full name
            val firstName = user.firstName
            val lastName = user.lastName
            var fullName: String?
            val fnSb = StringBuilder()
            if (firstName != null) fnSb.append(firstName)
            if (firstName != null || lastName != null) fnSb.append(' ')
            if (lastName != null) fnSb.append(lastName)
            fullName = fnSb.toString().trim { it <= ' ' }

            return PlayerDetailData(
                user.username,
                user.userId,
                user.avatar,
                fullName,
                user.chessTitle,
                item.country,
                null,  // followers - not in SearchItem
                null,  // fide - not in SearchItem
                user.createdAt,
                item.lastLoginDate,
                null,  // profileUrl - not in SearchItem // you can build this btw
                bullet, blitz, rapid, daily,
                null, null, null, null,  // bullet best/W/L/D
                null, null, null, null,  // blitz best/W/L/D
                null, null, null, null,  // rapid best/W/L/D
                null, null, null, null,  // daily best/W/L/D
                null, null,  // leaderboard rank/timeclass
                null  // favoriteSince
            )
        }

        @JvmStatic
        @JvmOverloads
        fun fromLeaderboardEntry(
            entry: LeaderboardEntry,
            timeclass: String? = null
        ): PlayerDetailData {
            // Extract W/L/D from leaderboard entry
            val record = entry.gameRecord
            val wins = record?.win
            val losses = record?.loss
            val draws = record?.draw

            // Determine which timeclass rating to populate based on timeclass parameter
            var bulletRating: Int? = null
            var blitzRating: Int? = null
            var rapidRating: Int? = null
            var dailyRating: Int? = null

            var bulletWins: Int? = null
            var bulletLosses: Int? = null
            var bulletDraws: Int? = null

            var blitzWins: Int? = null
            var blitzLosses: Int? = null
            var blitzDraws: Int? = null

            var rapidWins: Int? = null
            var rapidLosses: Int? = null
            var rapidDraws: Int? = null

            var dailyWins: Int? = null
            var dailyLosses: Int? = null
            var dailyDraws: Int? = null

            when (timeclass?.lowercase()) {
                "bullet" -> {
                    bulletRating = entry.score
                    bulletWins = wins
                    bulletLosses = losses
                    bulletDraws = draws
                }

                "blitz" -> {
                    blitzRating = entry.score
                    blitzWins = wins
                    blitzLosses = losses
                    blitzDraws = draws
                }

                "rapid" -> {
                    rapidRating = entry.score
                    rapidWins = wins
                    rapidLosses = losses
                    rapidDraws = draws
                }

                "daily" -> {
                    dailyRating = entry.score
                    dailyWins = wins
                    dailyLosses = losses
                    dailyDraws = draws
                }
            }

            return PlayerDetailData(
                entry.username,
                entry.playerId,
                entry.avatarUrl,
                entry.name,
                entry.title,
                null,  // entry.country returns url, need to fetch
                null,  // followers
                null,  // fide
                null,  // joined
                null,  // lastOnline
                entry.profilePage,
                bulletRating, blitzRating, rapidRating, dailyRating,
                null, bulletWins, bulletLosses, bulletDraws,  // bullet
                null, blitzWins, blitzLosses, blitzDraws,     // blitz
                null, rapidWins, rapidLosses, rapidDraws,     // rapid
                null, dailyWins, dailyLosses, dailyDraws,     // daily
                entry.rank, timeclass,  // leaderboard metadata
                null  // favoriteSince
            )
        }

        @JvmStatic
        fun fromPlayer(player: Player): PlayerDetailData {
            return PlayerDetailData(
                player.username,
                player.playerId,
                player.avatarUrl,
                player.name,
                player.title,
                null,  // country - fetch separately via getCountryInfo()
                player.followers,
                null,  // fide - in PlayerStats
                player.joined,
                player.lastOnline,
                player.profilePage,
                null, null, null, null,  // ratings in PlayerStats
                null, null, null, null,  // bullet best/W/L/D
                null, null, null, null,  // blitz best/W/L/D
                null, null, null, null,  // rapid best/W/L/D
                null, null, null, null,  // daily best/W/L/D
                null, null,  // leaderboard rank/timeclass
                null  // favoriteSince
            )
        }

        @JvmStatic
        fun fromPlayerStats(stats: PlayerStats): PlayerDetailData {
            return PlayerDetailData(
                null,  // username not in stats
                null, null, null, null, null, null,
                if (stats.fide > 0) stats.fide else null,
                null, null, null,
                stats.bullet?.last?.rating,
                stats.blitz?.last?.rating,
                stats.rapid?.last?.rating,
                stats.daily?.last?.rating,
                stats.bullet?.best?.rating,
                stats.bullet?.record?.win,
                stats.bullet?.record?.loss,
                stats.bullet?.record?.draw,
                stats.blitz?.best?.rating,
                stats.blitz?.record?.win,
                stats.blitz?.record?.loss,
                stats.blitz?.record?.draw,
                stats.rapid?.best?.rating,
                stats.rapid?.record?.win,
                stats.rapid?.record?.loss,
                stats.rapid?.record?.draw,
                stats.daily?.best?.rating,
                stats.daily?.record?.win,
                stats.daily?.record?.loss,
                stats.daily?.record?.draw,
                null, null,  // leaderboard rank/timeclass
                null  // favoriteSince
            )
        }

        @JvmStatic
        fun fromCountry(country: CountryInfo?): PlayerDetailData {
            return PlayerDetailData(
                null,
                null, null, null, null,
                country,
                null, null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null,
                null
            )
        }

        @JvmStatic
        fun fromFavorite(user: UserFavoriteModel) = PlayerDetailData(
            username = user.username,
            playerId = user.userId,
            title = user.title,
            favoriteSince = user.favoriteSince,
            lastOnline = user.lastLoginTime,

            avatar = null,
            name = null,
            country = null,
            followers = null,
            fide = null,
            joined = null,
            profileUrl = null,
            bulletRating = null,
            blitzRating = null,
            rapidRating = null,
            dailyRating = null,
            bulletBest = null,
            bulletWins = null,
            bulletLosses = null,
            bulletDraws = null,
            blitzBest = null,
            blitzWins = null,
            blitzLosses = null,
            blitzDraws = null,
            rapidBest = null,
            rapidWins = null,
            rapidLosses = null,
            rapidDraws = null,
            dailyBest = null,
            dailyWins = null,
            dailyLosses = null,
            dailyDraws = null,
            leaderboardRank = null,
            leaderboardTimeclass = null,
        )

    }
}



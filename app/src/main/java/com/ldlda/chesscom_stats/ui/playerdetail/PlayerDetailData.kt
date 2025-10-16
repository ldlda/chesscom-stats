package com.ldlda.chesscom_stats.ui.playerdetail

import com.ldlda.chesscom_stats.api.data.country.CountryInfo
import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry
import com.ldlda.chesscom_stats.api.data.player.Player
import com.ldlda.chesscom_stats.api.data.player.Title
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem
import com.ldlda.chesscom_stats.api.data.timeclass.Rule
import com.ldlda.chesscom_stats.api.data.timeclass.Time
import okhttp3.HttpUrl
import java.time.Instant

/**
 * Everything the PlayerDetailActivity needs to render.
 *
 * All fields nullable - progressively fill in as data arrives.
 *
 * Merge partial data from different sources (SearchItem, LeaderboardEntry, Player API, etc.)
 */
@JvmRecord
data class PlayerDetailData(
    val username: String?,
    val playerId: Long?,
    val avatar: HttpUrl?,
    val name: String?,
    val title: Title?,
    val country: CountryInfo?,
    val followers: Int?,
    val fide: Int?,
    val joined: Instant?,
    val lastOnline: Instant?,
    val profileUrl: HttpUrl?,
    val bulletRating: Int?,
    val blitzRating: Int?,
    val rapidRating: Int?,
    val dailyRating: Int?
) {
    // Minimal constructor - just username
    constructor(username: String?) : this(
        username,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    /**
     * Merge this data with another PlayerDetailData.
     *
     * Prefers non-null values from either source (`this` takes priority if both non-null).
     */
    fun mergeWith(other: PlayerDetailData): PlayerDetailData {
        return PlayerDetailData(
            this.username,  // username never changes
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
            this.dailyRating ?: other.dailyRating
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

    companion object {
        // ========== Factory Methods ==========
        fun fromSearchItem(item: SearchItem): PlayerDetailData {
            val user = item.userView

            // Extract ratings from SearchItem
            var bullet: Int? = null
            var blitz: Int? = null
            var rapid: Int? = null
            var daily: Int? = null
            for (rating in item.ratings) {
                val dog = rating.rating
                if (rating.variantClass != Rule.Chess) continue
                when (rating.variantTime) {
                    Time.Bullet -> bullet = dog
                    Time.Blitz -> blitz = dog
                    Time.Rapid -> rapid = dog
                    Time.Daily -> daily = dog
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
                bullet,
                blitz,
                rapid,
                daily
            )
        }

        fun fromLeaderboardEntry(entry: LeaderboardEntry): PlayerDetailData {
            // LeaderboardEntry has: username, playerId, avatar, country, profileUrl, rank, score
            // Score is for one time class - need to figure out which
            return PlayerDetailData(
                entry.username,
                entry.playerId,
                entry.avatarUrl,
                entry.name,
                entry.title,
                null,  // entry.country returns url
                null,  // followers
                null,  // fide
                null,  // joined
                null,  // lastOnline
                entry.profilePage,
                null,  // will be filled by timeclass-specific score
                null,
                null,
                null
            )
        }

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
                null,  // ratings in PlayerStats
                null,  // hint sometimes show... wtf.
                null,
                null
            )
        }

        fun fromPlayerStats(stats: PlayerStats): PlayerDetailData {
            return PlayerDetailData(
                null,  // username not in stats
                null,
                null,
                null,
                null,
                null,
                null,
                if (stats.fide > 0) stats.fide else null,
                null,
                null,
                null,
                stats.bullet?.last?.rating,
                stats.blitz?.last?.rating,
                stats.rapid?.last?.rating,
                stats.daily?.last?.rating
            )
        }

        fun fromCountry(username: String?, country: CountryInfo?): PlayerDetailData {
            return PlayerDetailData(
                username,
                null, null, null, null,  // LMAO
                country,
                null, null, null, null, null, null, null, null, null // MORE lmao
            )
        }
    }
}



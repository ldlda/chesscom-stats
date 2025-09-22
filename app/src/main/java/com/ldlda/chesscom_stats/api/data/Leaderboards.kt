@file:UseSerializers(URISerializer::class)

package com.ldlda.chesscom_stats.api.data

import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.utils.parse.URISerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URI

@Serializable
data class Leaderboards(
    @SerialName("live_blitz")
    val blitz: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_bullet")
    val bullet: List<LeaderboardEntry> = emptyList(),
    @SerialName("live_rapid")
    val rapid: List<LeaderboardEntry> = emptyList(),
    val tactics: List<LeaderboardEntry> = emptyList(),
)

/**
 *
 * docs is seriously Lacking

Very suspicious:
{
"player_id": 3889224,
"@id": "https://api.chess.com/pub/player/magnuscarlsen",
"url": "https://www.chess.com/member/MagnusCarlsen",
"username": "MagnusCarlsen",
"score": 3358,
"rank": 1,
"country": "https://api.chess.com/pub/country/NO",
"title": "GM",
"name": "Magnus Carlsen",
"status": "premium",
"avatar": "https://images.chesscomfiles.com/uploads/v1/user/3889224.121e2094.200x200o.361c2f8a59c2.jpg",
"trend_score": { // die
"direction": 0,
"delta": 0
},
"trend_rank": { // die
"direction": 0,
"delta": 0
},
"flair_code": "nothing", // die
"win_count": 4078,
"loss_count": 877,
"draw_count": 689
}

merge of gamerecord and some of players and elo (score) / rank
player also has: followers lastOnline joined
 */
@Serializable
data class LeaderboardEntry(
    val username: String,

    @SerialName("player_id")
    val playerId: Long,

    @SerialName("score")
    val elo: Int = 0,

    val rank: Int = 0,

    @SerialName("win_count") val winCount: Int? = null,
    @SerialName("loss_count") val lossCount: Int? = null,
    @SerialName("draw_count") val drawCount: Int? = null,


    @SerialName("url") val profilePage: URI? = null,
    @SerialName("country") val countryUrl: URI? = null,
    val status: String? = null,
    val title: String? = null,
    val name: String? = null,
    @SerialName("avatar") val avatarUrl: URI? = null,
) {
    val gameRecord
        get() =
            (winCount ?: drawCount ?: lossCount)
                ?.let {
                    GameRecord(
                        win = winCount ?: 0,
                        draw = drawCount ?: 0,
                        loss = lossCount ?: 0
                    )
                }

    // Optional: when you need the full Player from a leaderboard row
    suspend fun fetchPlayer(repo: ChessRepository) =
        repo.getPlayer(username)
}
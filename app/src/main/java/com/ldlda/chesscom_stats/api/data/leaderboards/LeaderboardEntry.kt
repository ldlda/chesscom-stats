@file:UseSerializers(InstantEpochSecondSerializer::class, HttpUrlSerializer::class)

package com.ldlda.chesscom_stats.api.data.leaderboards

import android.os.Parcelable
import com.ldlda.chesscom_stats.api.data.player.Status
import com.ldlda.chesscom_stats.api.data.player.Title
import com.ldlda.chesscom_stats.api.data.player.stats.Record
import com.ldlda.chesscom_stats.api.repository.ChessRepository
import com.ldlda.chesscom_stats.util.parcelize.httpurl.HttpUrlParceler
import com.ldlda.chesscom_stats.util.parcelize.httpurl.HttpUrlParcelerNullable
import com.ldlda.chesscom_stats.util.serialize.InstantEpochSecondSerializer
import com.ldlda.chesscom_stats.util.serialize.tostring.HttpUrlSerializer
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.WriteWith
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl

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
@Parcelize
@TypeParceler<HttpUrl, HttpUrlParceler>()
@Serializable
data class LeaderboardEntry(
    val username: String,

    @SerialName("player_id")
    val playerId: Long,

    val score: Int = 0,

    val rank: Int = 0,

    @SerialName("win_count") val winCount: Int? = null,
    @SerialName("loss_count") val lossCount: Int? = null,
    @SerialName("draw_count") val drawCount: Int? = null,


    @SerialName("url") val profilePage: HttpUrl,
    @SerialName("country") val countryUrl: @WriteWith<HttpUrlParcelerNullable> HttpUrl? = null,
    val status: Status? = Status.Basic,
    val title: Title? = null,
    val name: String? = null,
    @SerialName("avatar") val avatarUrl: @WriteWith<HttpUrlParcelerNullable> HttpUrl? = null, // HttpUrl("https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif"),
) : Parcelable {
    @IgnoredOnParcel
    val elo get() = score
    val gameRecord
        get() =
            (winCount ?: drawCount ?: lossCount)
                ?.let {
                    Record(
                        win = winCount ?: 0,
                        draw = drawCount ?: 0,
                        loss = lossCount ?: 0,
                    )
                }

    // Optional: when you need the full Player from a leaderboard row
    suspend fun fetchPlayer(repo: ChessRepository) =
        repo.getPlayer(username)

    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        @JvmStatic
        fun fromJSON(jsonString: String) =
            jsonFormat.decodeFromString(serializer(), jsonString)
    }

    fun toJSON() = jsonFormat.encodeToString(this)
}
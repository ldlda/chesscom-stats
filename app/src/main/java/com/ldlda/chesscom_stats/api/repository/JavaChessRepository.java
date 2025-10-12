package com.ldlda.chesscom_stats.api.repository;

import androidx.annotation.NonNull;

import com.ldlda.chesscom_stats.api.data.club.Club;
import com.ldlda.chesscom_stats.api.data.country.CountryInfo;
import com.ldlda.chesscom_stats.api.data.leaderboards.Leaderboards;
import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.HttpUrl;

/**
 * Java-friendly Chess.com API repository interface.
 * <p>
 * All methods return {@link CompletableFuture} for async operations without requiring
 * knowledge of Kotlin coroutines. For Kotlin callers who prefer suspend functions,
 * use {@link ChessRepository} directly or call {@code .await()} on the futures.
 * <p>
 * Implementation handles:
 * <ul>
 *   <li>TTL caching (2 min default)</li>
 *   <li>ETag conditional GET (when enabled)</li>
 *   <li>Rate limiting (prevents 429 errors)</li>
 *   <li>Exception mapping to {@link com.ldlda.chesscom_stats.api.fetch.ChessApiException}</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>{@code
 * JavaChessRepository repo = RepositoryProvider.getJavaRepository(context);
 * repo.getPlayer("hikaru")
 *     .thenAccept(player -> {
 *         // use player
 *     })
 *     .exceptionally(ex -> {
 *         Log.e("API", "Failed", ex);
 *         return null;
 *     });
 * }</pre>
 */
public interface JavaChessRepository {

    /**
     * Fetch a player's profile by username.
     * <p>
     * Cached for 2 minutes. Case-insensitive.
     *
     * @param username Chess.com username
     * @return Future containing the player profile
     */
    @NonNull
    CompletableFuture<Player> getPlayerAsync(@NonNull String username);

    /**
     * Fetch a player's game statistics (ratings, win/loss/draw counts).
     * <p>
     * Cached for 2 minutes.
     *
     * @param username Chess.com username
     * @return Future containing player stats
     */
    @NonNull
    CompletableFuture<PlayerStats> getPlayerStatsAsync(@NonNull String username);

    /**
     * Fetch global leaderboards (top players by rating category).
     * <p>
     * Cached for 2 minutes.
     *
     * @return Future containing leaderboards
     */
    @NonNull
    CompletableFuture<Leaderboards> getLeaderboardsAsync();

    /**
     * Fetch country information by URL (from a player's country field).
     * <p>
     * Prefer this over {@code getCountryByCode} when you have the URL from a player object.
     *
     * @param countryUrl Full URL to the country resource
     * @return Future containing country info
     */
    @NonNull
    CompletableFuture<CountryInfo> getCountryByUrlAsync(@NonNull HttpUrl countryUrl);

    /**
     * Search for player usernames with autocomplete.
     * <p>
     * <b>Warning:</b> This uses a private Chess.com endpoint that may break at any time.
     * Rate-limited to avoid abuse.
     *
     * @param prefix Username prefix to search for
     * @return Future containing list of matching usernames (max ~10 results)
     */
    @NonNull
    CompletableFuture<List<SearchItem>> searchPlayersAsync(@NonNull String prefix);

    // TODO: Add club/country endpoints once BSFactor migration completes
    @NonNull
    CompletableFuture<@NotNull Club> getClubAsync(@NonNull String nameId);

    @NonNull
    CompletableFuture<Club> getClubAsync(@NonNull HttpUrl clubUrl);

    @NonNull
    CompletableFuture<List<HttpUrl>> getCountryClubsAsync(@NonNull String code);
}

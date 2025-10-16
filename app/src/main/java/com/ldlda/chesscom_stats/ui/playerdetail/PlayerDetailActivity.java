package com.ldlda.chesscom_stats.ui.playerdetail;

import static androidx.lifecycle.LifecycleKt.getCoroutineScope;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.country.CountryInfo;
import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.Title;
import com.ldlda.chesscom_stats.api.data.player.stats.BaseRecord;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.data.player.stats.Stats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;
import com.ldlda.chesscom_stats.databinding.ActivityPlayerDetailBinding;
import com.ldlda.chesscom_stats.ui.favorites.FavoritesViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import okhttp3.HttpUrl;

public class PlayerDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYER_ENTRY = "player_entry";
    public static final String EXTRA_USERNAME = "username"; // Fallback for legacy
    public static final String EXTRA_TIMECLASS = "timeclass"; // Which leaderboard it came from (blitz/bullet/rapid/daily)

    private final String TAG = "PlayerDetailActivity";
    private ChessRepoAdapterJava<ChessRepositoryTimedCache> repo;
    private FavoritesViewModel favoritesViewModel;
    // Specify type parameter for CompletableFuture
    private CompletableFuture<Void> inFlight;
    private ActivityPlayerDetailBinding binding;

    private String username;
    private Long playerId;
    static final String noData = "No data";
    private HttpUrl profileUrl;
    private TextView usernameView;
    private TextView nameView;
    private String timeclass; // "blitz", "bullet", "rapid", or "daily"
    // View references
    private ImageView avatar;
    private TextView titleView;
    private TextView countryView;
    private TextView bulletScores;
    private TextView blitzScores;
    private TextView rapidScores;
    private TextView fideScore;
    private TextView followerCount;
    private TextView joinedDate;
    private TextView lastOnlineDate;
    private MaterialButton addFavoriteBtn;
    private MaterialButton profileButton;

    private String curtitle;
    private long curlastOnlineDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Bind views
        avatar = binding.playerDetailAvatar;
        usernameView = binding.playerDetailUsername;
        nameView = binding.playerDetailName;
        titleView = binding.title;
        countryView = binding.country;
        bulletScores = binding.bulletScores;
        blitzScores = binding.blitzScores;
        rapidScores = binding.rapidScores;
        fideScore = binding.fideScore;
        followerCount = binding.followerCount;
        joinedDate = binding.joinedDate;
        lastOnlineDate = binding.lastOnlineDate;
        addFavoriteBtn = binding.addToFavBtn;
        profileButton = binding.profileURI;

        // Initialize repository early (needed for both fast and slow paths)
        repo = ChessRepoAdapterJava.getAdapterJava(new ChessRepositoryTimedCache(),
                getCoroutineScope(getLifecycle()));

        // Try to get LeaderboardEntry first (optimized path)
        LeaderboardEntry playerEntry = getIntent().getParcelableExtra(EXTRA_PLAYER_ENTRY);
        timeclass = getIntent().getStringExtra(EXTRA_TIMECLASS); // May be null

        if (playerEntry != null) {
            // ========== FAST PATH ==========
            // We came from leaderboards - already have cached data in the Parcelable!
            // Advantage: Show UI INSTANTLY (no loading spinner), fetch rest in background
            // Note: Still fetches same 3 APIs (player, stats, country) - but UI shows immediately
            //       In pure Kotlin this wouldn't be needed (suspend functions FTW), but Java AsyncTask ass
            username = playerEntry.getUsername();
            playerId = playerEntry.getPlayerId();
            profileUrl = playerEntry.getProfilePage();

            // Display what we already have immediately (no loading spinner!)
            displayPlayerEntry(playerEntry, timeclass);

            // Then fetch only the missing data in background
            fetchPlayerStats();
        } else {
            // ========== SLOW PATH ==========
            // We came from search or direct link - have NOTHING except username
            // Must wait for API before showing anything (loading spinner shows)
            username = getIntent().getStringExtra(EXTRA_USERNAME);
            if (username == null) {
                throw new IllegalArgumentException("Intent extra 'username' or 'player_entry' must not be null");
            }

            usernameView.setText(username);
            // Fetch everything from API (shows loading states)
            fetchPlayerData();
        }

        // Check if favorited (background thread via ViewModel)
        if (playerId != null) {
            favoritesViewModel.isFavorite(playerId, isFav -> {
                runOnUiThread(() -> {
                    updateFavoriteButton(isFav);
                });
                return null;
            });
        }

        // Favorite button click handler
        addFavoriteBtn.setOnClickListener(v -> {
            if (playerId != null && username != null) {
                String lastOnlineDateStr = String.valueOf(curlastOnlineDate); // Convert long to String
                favoritesViewModel.toggleFavorite(playerId, username, curtitle, lastOnlineDateStr, isFav -> {
                    runOnUiThread(() -> {
                        updateFavoriteButton(isFav);
                        Toast.makeText(this,
                                isFav ? "Added to favorites" : "Removed from favorites",
                                Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
            } else {
                Toast.makeText(this, "Player data not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Profile button click handler
        profileButton.setOnClickListener(v -> {
            if (profileUrl != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl.toString()));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Profile URL not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteButton(boolean isFavorited) {
        addFavoriteBtn.setText(isFavorited ? R.string.remove_fav : R.string.add_fav);
        addFavoriteBtn.setIcon(AppCompatResources.getDrawable(getApplicationContext(), isFavorited ? R.drawable.ic_unfav : R.drawable.ic_favorite));
    }

    private String formatInstant(java.time.Instant instant) {
        if (instant == null) return "Unknown";
        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * SLOW PATH: Fetch everything from scratch when we only have a username
     * <p>
     * This is used when navigating from search or a direct link
     * <p>
     * Makes 1 API call: getCompletePlayerAsync (which internally fetches player + stats + country)
     */
    private void fetchPlayerData() {
        inFlight = repo.getCompletePlayerAsync(username)
                .thenAccept(player -> {
                    if (isFinishing() || isDestroyed()) return;

                    // getCompletePlayerAsync already fetches stats + country, so just use them
                    PlayerStats playerStats = player.getPlayerStats();
                    CountryInfo country = player.getCountryInfo();
                    if (playerStats == null || country == null) {
                        throw new IllegalStateException("getCompletePlayerAsync didn't fetch stats/country");
                    }

                    // Capture for other uses
                    playerId = player.getPlayerId();
                    profileUrl = player.getProfilePage();

                    runOnUiThread(() -> {
                        // Update favorites button now that we have playerId
                        favoritesViewModel.isFavorite(playerId, isFav -> {
                            runOnUiThread(() -> updateFavoriteButton(isFav));
                            return null;
                        });

                        // Avatar
                        HttpUrl avatarUri = player.getAvatarUrl();
                        if (avatarUri != null) {
                            Picasso.get()
                                    .load(avatarUri.toString())
                                    .placeholder(R.drawable.ic_person)
                                    .error(R.drawable.ic_person)
                                    .into(avatar);
                        } else {
                            avatar.setImageResource(R.drawable.ic_person);
                        }

                        // Name
                        String name = player.getName();
                        if (name != null && !name.isEmpty()) {
                            nameView.setText(name);
                        }

                        // Title
                        Title title = player.getTitle();
                        titleView.setText(title != null ? title.getDisplayName() : getString(R.string.none));
                        curtitle = title != null ? title.getDisplayName() : "none";

                        // Country (already fetched!)
                        countryView.setText(country.getName());

                        // FIDE
                        int fide = playerStats.getFide();
                        fideScore.setText(String.format("FIDE: %s", fide > 0 ? String.valueOf(fide) : "N/A"));

                        // Followers
                        followerCount.setText(String.format("Followers: %d", player.getFollowers()));

                        // Joined & Last Online
                        joinedDate.setText(formatInstant(player.getJoined()));
                        lastOnlineDate.setText(formatInstant(player.getLastOnline()));
                        // Fix type mismatch for curlastOnlineDate
                        curlastOnlineDate = player.getLastOnline() != null ? player.getLastOnline().toEpochMilli() : 5555L;

                        // Stats (already fetched!)
                        updateStatsViews(playerStats);
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
                    Log.e(TAG, "Failed to fetch player data", ex);
                    runOnUiThread(() -> {
                        Toast.makeText(this, R.string.failed_to_load_player_data, Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
    }

    /**
     * FAST PATH: Display what we already have from LeaderboardEntry (instant - no API call!)
     * <p>
     * This is used when clicking from leaderboards - we already have cached data
     * <p>
     * UX Win: User sees content IMMEDIATELY instead of waiting for API
     * <p>
     * Technical: Still fetches player/stats/country in background (same as slow path)
     *           but UI updates incrementally instead of all-at-once
     *
     * @param timeclass Which leaderboard this came from: "blitz", "bullet", "rapid", or "daily"
     */
    private void displayPlayerEntry(LeaderboardEntry entry, @Nullable String timeclass) {
        // Username
        usernameView.setText(entry.getUsername());

        // Avatar
        HttpUrl avatarUri = entry.getAvatarUrl();
        if (avatarUri != null) {
            Picasso.get()
                    .load(avatarUri.toString())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(avatar);
        } else {
            avatar.setImageResource(R.drawable.ic_person);
        }

        // Name
        String name = entry.getName();
        if (name != null && !name.isEmpty()) {
            nameView.setText(name);
        }

        // Title
        Title title = entry.getTitle();
        titleView.setText(title != null ? title.getDisplayName() : getString(R.string.none));

        // Country (will be updated when fetchCountryInfo completes)
        HttpUrl countryUrl = entry.getCountryUrl();
        if (countryUrl != null) {
            fetchCountryInfo(countryUrl);
        }

        // Show basic info from leaderboard entry
        // Use the correct card based on which timeclass leaderboard it came from
        String leaderboardLabel = timeclass != null ? timeclass.substring(0, 1).toUpperCase() + timeclass.substring(1) : "Rating";
        String leaderboardText = String.format("%s\nRating: %d\nRank: #%d", leaderboardLabel, entry.getScore(), entry.getRank());

        // Add W/L/D record if available
        var record = entry.getGameRecord();
        if (record != null) {
            leaderboardText += String.format("\nW/L/D\n%d / %d / %d", record.getWin(), record.getLoss(), record.getDraw());
        }

        // Put the leaderboard data in the correct card
        if ("bullet".equalsIgnoreCase(timeclass)) {
            bulletScores.setText(leaderboardText);
            blitzScores.setText("Blitz\nLoading...");
            rapidScores.setText("Rapid\nLoading...");
        } else if ("rapid".equalsIgnoreCase(timeclass)) {
            rapidScores.setText(leaderboardText);
            bulletScores.setText("Bullet\nLoading...");
            blitzScores.setText("Blitz\nLoading...");
        } else if ("daily".equalsIgnoreCase(timeclass)) {
            // Daily doesn't have a dedicated card, use rapid card
            rapidScores.setText(leaderboardText);
            bulletScores.setText("Bullet\nLoading...");
            blitzScores.setText("Blitz\nLoading...");
        } else {
            // Default to blitz (null or "blitz")
            blitzScores.setText(leaderboardText);
            bulletScores.setText("Bullet\nLoading...");
            rapidScores.setText("Rapid\nLoading...");
        }
    }

    /**
     * FAST PATH (part 2): Fetch remaining data after showing cached LeaderboardEntry
     * Makes 2 API calls in parallel:
     * <ol>
     *     <li>
     *   getPlayerStatsAsync - for FIDE + detailed bullet/blitz/rapid stats
     *</li><li>
     *  getPlayerAsync - for followers count
     * </li></ol><p>
     * Not actually cheaper API-wise (same 2 calls as getCompletePlayerAsync)
     * <p>
     * Real win: UI already showed, this just fills in the gaps (perceived speed)
     */
    private void fetchPlayerStats() {
        // Fetch stats for FIDE + rating details
        CompletableFuture<PlayerStats> statsFuture = repo.getPlayerStatsAsync(username);

        // Fetch player for followers count
        CompletableFuture<Player> playerFuture = repo.getPlayerAsync(username);

        inFlight = CompletableFuture.allOf(statsFuture, playerFuture)
                .thenAccept(ignored -> {
                    if (isFinishing() || isDestroyed()) return;

                    PlayerStats playerStats = statsFuture.join();
                    Player player = playerFuture.join();

                    runOnUiThread(() -> {
                        // FIDE rating
                        int fide = playerStats.getFide();
                        fideScore.setText(String.format("FIDE: %s", fide > 0 ? String.valueOf(fide) : "N/A"));

                        // Followers
                        followerCount.setText(String.format("Followers: %d", player.getFollowers()));

                        // Joined & Last Online
                        joinedDate.setText(formatInstant(player.getJoined()));
                        lastOnlineDate.setText(formatInstant(player.getLastOnline()));

                        // Update stat cards
                        updateStatsViews(playerStats);
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
                    Log.e(TAG, "Failed to fetch stats", ex);
                    return null;
                });
    }

    /**
     * Update the three stat cards with bullet/blitz/rapid data
     * DX improvement: ONE place to update stats instead of 3!
     * Shows rating + W/L/D record for all 3 cards
     */
    private void updateStatsViews(PlayerStats stats) {
        updateStatCard(bulletScores, "Bullet", stats.getBullet());
        updateStatCard(blitzScores, "Blitz", stats.getBlitz());
        updateStatCard(rapidScores, "Rapid", stats.getRapid());
    }

    /**
     * Helper to update a single stat card with rating + W/L/D
     */
    private void updateStatCard(TextView view, String label, @org.jetbrains.annotations.Nullable Stats<? extends BaseRecord> stat) {
        if (stat == null) {
            view.setText(String.format("%s\n%s", label, noData));
            return;
        }

        StringBuilder text = new StringBuilder(label).append("\n");
        text.append(String.format("Current: %d\n", stat.getLast().getRating()));

        if (stat.getBest() != null)
            text.append(String.format("Best: %d\n", stat.getBest().getRating()));

        var record = stat.getRecord();
        text.append(String.format("W/L/D\n%d / %d / %d", record.getWin(), record.getLoss(), record.getDraw()));

        view.setText(text.toString());
    }

    /**
     * Fetch country info by URL and update the country TextView
     */
    private void fetchCountryInfo(HttpUrl countryUrl) {
        repo.getCountryByUrlAsync(countryUrl)
                .thenAccept(country -> {
                    if (isFinishing() || isDestroyed()) return;
                    runOnUiThread(() -> countryView.setText(country.getName()));
                })
                .exceptionally(ex -> {
                    Log.e(TAG, "Failed to fetch country", ex);
                    return null;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inFlight != null) inFlight.cancel(true);
        if (repo != null) repo.close();
    }
}

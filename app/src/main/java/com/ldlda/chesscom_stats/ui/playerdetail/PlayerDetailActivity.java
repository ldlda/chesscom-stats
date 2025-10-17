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
import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.Title;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepository;
import com.ldlda.chesscom_stats.databinding.ActivityPlayerDetailBinding;
import com.ldlda.chesscom_stats.di.RepoProvider;
import com.ldlda.chesscom_stats.ui.favorites.FavoritesViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import okhttp3.HttpUrl;

public class PlayerDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYER_DATA = "player_data";
    public static final String EXTRA_USERNAME = "username"; // Fallback for legacy callers
    private final String TAG = "PlayerDetailActivity";
    private ChessRepoAdapterJava<@NotNull ChessRepository> repo;
    private FavoritesViewModel favoritesViewModel;
    private CompletableFuture<?> inFlight;
    private ActivityPlayerDetailBinding binding;
    private PlayerDetailData currentData;
    private boolean isFullyLoaded = false; // True after all API fetches complete
    // View references
    private ImageView avatar;
    private TextView usernameView;
    private TextView nameView;
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

        // Initialize repository
        repo = RepoProvider.defaultRepository(getApplicationContext()).buildJavaAdapter(
                getCoroutineScope(getLifecycle()));

        // ========== UNIFIED PATH ==========
        // Get PlayerDetailData from Intent (all sources send this now)
        PlayerDetailData data = getIntent().getParcelableExtra(EXTRA_PLAYER_DATA);

        // Fallback for legacy callers that only send username
        if (data == null) {
            String username = getIntent().getStringExtra(EXTRA_USERNAME);
            if (username == null) {
                throw new IllegalArgumentException("Intent must include EXTRA_PLAYER_DATA or EXTRA_USERNAME");
            }
            data = new PlayerDetailData(username);
        }

        currentData = data;

        // Render what we have immediately (progressive enhancement)
        render(data);

        // Check if favorited (background thread via ViewModel)
        Long playerId = data.playerId();
        if (playerId != null) {
            favoritesViewModel.isFavorite(playerId, isFav -> {
                runOnUiThread(() -> updateFavoriteButton(isFav));
                return null;
            });
        }

        // Fetch missing data in background
        fetchMissingData(data);

        // Favorite button click handler
        addFavoriteBtn.setOnClickListener(v -> {
            Long playerId1 = currentData.playerId();
            String username1 = currentData.username();
            if (playerId1 != null && username1 != null) {
                favoritesViewModel.toggleFavorite(playerId1, username1, isFav -> {
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
            HttpUrl profileUrl = currentData.profileUrl();
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
     * Render PlayerDetailData to UI (progressive enhancement pattern)
     * Shows whatever data is available, leaves rest as placeholders
     */
    private void render(PlayerDetailData data) {
        // Username
        if (data.username() != null) {
            usernameView.setText(data.username());
        }

        // Avatar
        HttpUrl avatar1 = data.avatar();
        if (avatar1 != null) {
            Picasso.get()
                    .load(avatar1.toString())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(avatar);
        } else {
            avatar.setImageResource(R.drawable.ic_person);
        }

        // Name
        String name = data.name();
        if (name != null && !name.isEmpty()) { // LINT WHY ARE YOU STUPID
            nameView.setText(name);
        } else {
            nameView.setText("--");
        }

        // Title
        Title title = data.title();
        if (title != null) {
            titleView.setText(title.getDisplayName());
        } else {
            titleView.setText(getString(R.string.none));
        }

        // Country
        CountryInfo country = data.country();
        if (country != null) {
            countryView.setText(country.getName());
        } else {
            countryView.setText(isFullyLoaded ? "Unknown" : "Loading...");
        }

        // FIDE
        Integer fide = data.fide();
        if (fide != null) {
            fideScore.setText(String.format("FIDE: %s", fide > 0 ? String.valueOf(fide) : "N/A"));
        } else {
            fideScore.setText(isFullyLoaded ? "FIDE: N/A" : "FIDE: Loading...");
        }

        // Followers
        if (data.followers() != null) {
            followerCount.setText(String.format("Followers: %d", data.followers()));
        } else {
            followerCount.setText(isFullyLoaded ? "Followers: N/A" : "Followers: --");
        }

        // Joined & Last Online
        joinedDate.setText(formatInstant(data.joined()));
        lastOnlineDate.setText(formatInstant(data.lastOnline()));

        // Ratings - show what we have (current, best, W/L/D)
        updateRatingCard(bulletScores, "Bullet", data);
        updateRatingCard(blitzScores, "Blitz", data);
        updateRatingCard(rapidScores, "Rapid", data);
    }

    /**
     * Update a rating card with current/best rating + W/L/D record
     * Shows leaderboard rank if this timeclass matches the source leaderboard
     */
    private void updateRatingCard(TextView view, String label, PlayerDetailData data) {
        Integer current = null;
        Integer best = null;
        Integer wins = null;
        Integer losses = null;
        Integer draws = null;
        boolean isLeaderboardTimeclass = false;

        // Extract timeclass-specific data
        switch (label) {
            case "Bullet":
                current = data.bulletRating();
                best = data.bulletBest();
                wins = data.bulletWins();
                losses = data.bulletLosses();
                draws = data.bulletDraws();
                isLeaderboardTimeclass = "bullet".equalsIgnoreCase(data.leaderboardTimeclass());
                break;
            case "Blitz":
                current = data.blitzRating();
                best = data.blitzBest();
                wins = data.blitzWins();
                losses = data.blitzLosses();
                draws = data.blitzDraws();
                isLeaderboardTimeclass = "blitz".equalsIgnoreCase(data.leaderboardTimeclass());
                break;
            case "Rapid":
                current = data.rapidRating();
                best = data.rapidBest();
                wins = data.rapidWins();
                losses = data.rapidLosses();
                draws = data.rapidDraws();
                isLeaderboardTimeclass = "rapid".equalsIgnoreCase(data.leaderboardTimeclass());
                break;
        }

        // Build card text
        StringBuilder text = new StringBuilder(label).append("\n");

        // Show leaderboard rank if this is the source timeclass
        if (isLeaderboardTimeclass && data.leaderboardRank() != null) {
            text.append(String.format("Rank: #%d\n", data.leaderboardRank()));
        }

        if (current != null) {
            text.append(String.format("Current: %d\n", current));
        } else {
            text.append(isFullyLoaded ? "N/A\n" : "Loading...\n");
        }

        if (best != null) {
            text.append(String.format("Best: %d\n", best));
        } else if (isFullyLoaded && current != null) {
            // Only show "N/A" for best if we have a current rating (player plays this timeclass)
            text.append("Best: N/A\n");
        }

        if (wins != null || losses != null || draws != null) {
            text.append(String.format("W/L/D\n%d / %d / %d",
                    wins != null ? wins : 0,
                    losses != null ? losses : 0,
                    draws != null ? draws : 0));
        } else if (isFullyLoaded && current != null) {
            text.append("W/L/D\nN/A");
        }

        view.setText(text.toString());
    }

    /**
     * Fetch missing data based on what's null in PlayerDetailData
     * Uses the smart requires*() methods to avoid unnecessary API calls
     */
    private void fetchMissingData(PlayerDetailData data) {
        String username = data.username();
        if (username == null) {
            Log.e(TAG, "Cannot fetch data without username");
            return;
        }

        CompletableFuture<Player> playerFuture = null;
        CompletableFuture<PlayerStats> statsFuture = null;
        CompletableFuture<CountryInfo> countryFuture = null;

        // Fetch player if needed
        if (data.requiresPlayer()) {
            playerFuture = repo.getPlayerAsync(username);
        }

        // Fetch stats if needed
        if (data.requiresStats()) {
            statsFuture = repo.getPlayerStatsAsync(username);
        }

        // Fetch country if needed (requires player data first to get country URL)
        if (data.requiresCountry() && data.requiresPlayer()) {
            // Will fetch after player data arrives
        }

        // Collect all futures that are not null
        var futures = new java.util.ArrayList<CompletableFuture<?>>();
        if (playerFuture != null) futures.add(playerFuture);
        if (statsFuture != null) futures.add(statsFuture);

        if (futures.isEmpty()) {
            // Nothing to fetch - already have everything!
            isFullyLoaded = true;
            render(currentData); // Re-render to show "N/A" instead of "Loading..."
            return;
        }

        CompletableFuture<Player> finalPlayerFuture = playerFuture;
        CompletableFuture<PlayerStats> finalStatsFuture = statsFuture;

        inFlight = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(ignored -> {
                    if (isFinishing() || isDestroyed()) return;


                    // Merge player data
                    if (finalPlayerFuture != null) {
                        Player player = finalPlayerFuture.join();
                        currentData = currentData.mergeWith(PlayerDetailData.fromPlayer(player));

                        // Fetch country if still needed
                        if (currentData.requiresCountry()) {
                            // this will atomically update country field independent of all ts
                            fetchCountry(player.getCountryUrl());
                        }
                    }

                    // Merge stats data
                    if (finalStatsFuture != null) {
                        PlayerStats stats = finalStatsFuture.join();
                        currentData = currentData.mergeWith(PlayerDetailData.fromPlayerStats(stats));
                    }

                    isFullyLoaded = true; // Mark as fully loaded

                    // Re-render with enriched data
                    PlayerDetailData finalEnriched = currentData;
                    runOnUiThread(() -> {
                        render(finalEnriched);
                        Log.d(TAG, "fetchMissingData: current " + currentData);
                        // Update favorite button if we just got playerId
                        Long playerId = finalEnriched.playerId();
                        if (playerId != null) {
                            favoritesViewModel.isFavorite(playerId, isFav -> {
                                runOnUiThread(() -> updateFavoriteButton(isFav));
                                return null;
                            });
                        }
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
                    Log.e(TAG, "Failed to fetch player data", ex);
                    isFullyLoaded = true; // Mark as done even on error
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Failed to load player data", Toast.LENGTH_SHORT).show();
                        render(currentData); // Re-render to show "N/A" instead of "Loading..."
                    });
                    return null;
                });
    }

    /**
     * Fetch country info separately (happens after player fetch)
     */
    private void fetchCountry(HttpUrl countryUrl) {
        repo.getCountryByUrlAsync(countryUrl)
                .thenAccept(country -> {
                    if (isFinishing() || isDestroyed()) return;
                    Log.d(TAG, "fetchCountry: run");
                    currentData = currentData.mergeWith(
                            PlayerDetailData.fromCountry(country)
                    );

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
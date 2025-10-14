package com.ldlda.chesscom_stats.ui.playerdetail;

import static androidx.lifecycle.LifecycleKt.getCoroutineScope;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.country.CountryInfo;
import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.data.player.Title;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;
import com.ldlda.chesscom_stats.databinding.ActivityPlayerDetailBinding;
import com.ldlda.chesscom_stats.ui.favorites.FavoritesViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import okhttp3.HttpUrl;

public class PlayerDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYER_ENTRY = "player_entry";
    public static final String EXTRA_USERNAME = "username"; // Fallback for legacy
    
    private final String TAG = "PlayerDetailActivity";
    private ChessRepoAdapterJava<ChessRepositoryTimedCache> repo;
    private FavoritesViewModel favoritesViewModel;
    private CompletableFuture inFlight;
    private ActivityPlayerDetailBinding binding;
    private String username;
    private Long playerId;
    private ImageView avatar;
    private TextView usernameView;
    private TextView nameView;
    private TextView statsView;

    private Button addFavoriteBtn;

    private String formatInstant(java.time.Instant instant) {
        if (instant == null) return "";
        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        avatar = binding.playerDetailAvatar;
        usernameView = binding.playerDetailUsername;
        nameView = binding.playerDetailName;
        statsView = binding.playerDetailStats;
        addFavoriteBtn = binding.addToFavBtn;

        // Initialize repository early (needed for both fast and slow paths)
        repo = ChessRepoAdapterJava.getAdapterJava(new ChessRepositoryTimedCache(),
                getCoroutineScope(getLifecycle()));

        // Try to get LeaderboardEntry first (optimized path)
        LeaderboardEntry playerEntry = getIntent().getParcelableExtra(EXTRA_PLAYER_ENTRY);

        if (playerEntry != null) {
            // Fast path: we already have most of the data!
            username = playerEntry.getUsername();
            playerId = playerEntry.getPlayerId();

            // Display what we already have immediately
            displayPlayerEntry(playerEntry);

            // Then fetch only the missing detailed stats
            fetchPlayerStats();
        } else {
            // Fallback: old way (just username)
            username = getIntent().getStringExtra(EXTRA_USERNAME);
            if (username == null) {
                throw new IllegalArgumentException("Intent extra 'username' or 'player_entry' must not be null");
            }

            usernameView.setText(username);
            // Fetch everything from API
            fetchPlayerData();
        }

        // Check if favorited (background thread via ViewModel)
        if (playerId != null) {
            favoritesViewModel.isFavorite(playerId, isFav -> {
                runOnUiThread(() -> {
                    addFavoriteBtn.setText(isFav ? R.string.remove_fav : R.string.add_fav);
                });
                return null;
            });
        }

        // Favorite button click handler
        addFavoriteBtn.setOnClickListener(v -> {
            if (playerId != null && username != null) {
                favoritesViewModel.toggleFavorite(playerId, username, isFav -> {
                    runOnUiThread(() -> {
                        addFavoriteBtn.setText(isFav ? R.string.remove_fav : R.string.add_fav);
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
    }

    private void fetchPlayerData() {
        inFlight = repo.getCompletePlayerAsync(username)
                .thenAccept(player -> {
//                    Log.d(TAG, "onCreate: Constructed Player: " + player.toJSON());
                    if (isFinishing() || isDestroyed()) return;

                    // Capture playerId for favorites
                    playerId = player.getPlayerId();
                    
                    runOnUiThread(() -> {
                        // Update favorites button now that we have playerId
                        if (playerId != null) {
                            favoritesViewModel.isFavorite(playerId, isFav -> {
                                runOnUiThread(() -> {
                                    addFavoriteBtn.setText(isFav ? R.string.remove_fav : R.string.add_fav);
                                });
                                return null;
                            });
                        }
                        
                        // Avatar (null/placeholder-safe)
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
                        if (name != null && !name.isEmpty())
                            nameView.setText(name);

                        // Stats text (title, country URL, joined, last online, status)
                        StringBuilder stats = new StringBuilder();

                        Title title = player.getTitle();
                        if (title != null)
                            stats.append(String.format("%s: %s\n", getString(R.string.player_title), title));

                        // Build and set the base stats first
                        CountryInfo country = player.getCountryInfo();
                        if (country != null)
//                            Log.d(TAG, "onCreate: CountryInfo: " + country.toJSON());
                            stats.append(String.format("%s: %s\n", getString(R.string.player_country), country.getName()));

                        Instant joined = player.getJoined();
                        stats.append(String.format("%s: %s\n", getString(R.string.player_joined), formatInstant(joined)));

                        Instant lastOnline = player.getLastOnline();
                        stats.append(String.format("%s: %s\n", getString(R.string.player_last_online), formatInstant(lastOnline)));

                        String status = player.getStatus();
                        if (!status.isEmpty())
                            stats.append(String.format("%s: %s\n", getString(R.string.player_status), status));

                        // Player stats
                        PlayerStats playerStats = player.getPlayerStats();
                        if (playerStats != null) {
//                            Log.d(TAG, "onCreate: Player Stats: " + playerStats.toJSON());
                            var blitz = playerStats.getBlitz();
                            if (blitz != null) {
                                stats.append("Blitz (3 minute):\n");
                                if (blitz.getBest() != null)
                                    stats.append("\t" + "Best ELO: ")
                                            .append(blitz.getBest().getRating()) // .append(" elo in ")
//                                        .append(formatInstant(blitz.getBest().getDate()))
                                            .append("\n");
                                stats.append("\t" + "Games won: ").append(blitz.getRecord().getWin()).append("\n");
                            }
                        }

                        statsView.setText(stats.toString());
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
                    Log.e(TAG, "onCreate: Failed to fetch", ex);
                    runOnUiThread(() -> statsView.setText(R.string.failed_to_load_player_data));
                    return null;
                });
    }

    /**
     * Display player data from LeaderboardEntry (fast path - no API call needed for basic info)
     */
    private void displayPlayerEntry(LeaderboardEntry entry) {
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

        // Build stats from what we have
        StringBuilder stats = new StringBuilder();

        // Title
        Title title = entry.getTitle();
        if (title != null) {
            stats.append(String.format("%s: %s\n", getString(R.string.player_title), title));
        }

        // Rank and Rating (score)
        stats.append(String.format("Rank: #%d\n", entry.getRank()));
        stats.append(String.format("Rating: %d\n", entry.getScore()));

        // Game record (if available)
        var record = entry.getGameRecord();
        if (record != null) {
            stats.append(String.format("Games: %d W / %d L / %d D\n",
                    record.getWin(), record.getLoss(), record.getDraw()));
        }

        // Status
        String status = entry.getStatus();
        if (status != null && !status.isEmpty()) {
            stats.append(String.format("%s: %s\n", getString(R.string.player_status), status));
        }

        statsView.setText(stats.toString());

        // TODO: Fetch country info if countryUrl exists
        HttpUrl countryUrl = entry.getCountryUrl();
        if (countryUrl != null) {
            fetchCountryInfo(countryUrl);
        }
    }

    /**
     * Fetch only the detailed player stats (blitz/rapid breakdown)
     * Used when we already have basic info from LeaderboardEntry
     */
    private void fetchPlayerStats() {
        inFlight = repo.getPlayerStatsAsync(username)
                .thenAccept(playerStats -> {
                    if (isFinishing() || isDestroyed()) return;
                    runOnUiThread(() -> {
                        // Append detailed stats to existing text
                        StringBuilder stats = new StringBuilder(statsView.getText().toString());
                        stats.append("\n--- Detailed Stats ---\n");

                        var blitz = playerStats.getBlitz();
                        if (blitz != null) {
                            stats.append("Blitz (3 minute):\n");
                            if (blitz.getBest() != null) {
                                stats.append("\tBest ELO: ")
                                        .append(blitz.getBest().getRating())
                                        .append("\n");
                            }
                            stats.append("\tGames won: ").append(blitz.getRecord().getWin()).append("\n");
                        }

                        statsView.setText(stats.toString());
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
                    Log.e(TAG, "Failed to fetch stats", ex);
                    return null;
                });
    }

    /**
     * Fetch country info by URL and update the display
     */
    private void fetchCountryInfo(HttpUrl countryUrl) {
        repo.getCountryByUrlAsync(countryUrl)
                .thenAccept(country -> {
                    if (isFinishing() || isDestroyed()) return;
                    runOnUiThread(() -> {
                        // Insert country into stats text
                        String currentStats = statsView.getText().toString();
                        String countryLine = String.format("%s: %s\n",
                                getString(R.string.player_country), country.getName());

                        // Insert after title if exists, otherwise at beginning
                        int insertPos = currentStats.indexOf("Rank:");
                        if (insertPos > 0) {
                            currentStats = currentStats.substring(0, insertPos) +
                                    countryLine +
                                    currentStats.substring(insertPos);
                        } else {
                            currentStats = countryLine + currentStats;
                        }

                        statsView.setText(currentStats);
                    });
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

package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ldlda.chesscom_stats.api.data.CountryInfo;
import com.ldlda.chesscom_stats.api.data.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava;
import com.ldlda.chesscom_stats.databinding.ActivityPlayerDetailBinding;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class PlayerDetailActivity extends AppCompatActivity {
    private final String TAG = "PlayerDetailActivity";
    private ChessRepositoryJava repo;
    private CompletableFuture<Void> inFlight;

    private ActivityPlayerDetailBinding binding;

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

        ImageView avatar = binding.playerDetailAvatar;
        TextView usernameView = binding.playerDetailUsername;
        TextView nameView = binding.playerDetailName;
        TextView statsView = binding.playerDetailStats;

        // the biggest bullshit is that i cant export the damn leaderboard entry it already had stuff
        String username = getIntent().getStringExtra("username");

        if (username == null) {
            throw new IllegalArgumentException("Intent extra 'username' must not be null");
        }
/*  Use that to prime the view before loading everything

        String leaderboardEntry = getIntent().getStringExtra("leaderboard_entry");
        if (leaderboardEntry != null) {
            Log.d(TAG, "onCreate: leaderboard entry is " + leaderboardEntry);
            LeaderboardEntry entry = LeaderboardEntry.fromJSON(leaderboardEntry);
            runOnUiThread(() -> {
                // Avatar (null/placeholder-safe)
                URI avatarUri = entry.getAvatarUrl();
                if (avatarUri != null) {
                    Picasso.get()
                            .load(avatarUri.toString())
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(avatar);
                } else {
                    avatar.setImageResource(R.drawable.ic_person);
                }
                String name = entry.getName();
                if (name != null && !name.isEmpty())
                    nameView.setText(name);
                StringBuilder stats = new StringBuilder();
                String title = entry.getTitle();
                if (title != null && !title.isEmpty())
                    stats.append("Title: ").append(title).append("\n");
                String status = entry.getStatus();
                if (status != null && !status.isEmpty())
                    stats.append("Status: ").append(status).append("\n");

                statsView.setText(stats.toString());
            });
        }
*/

        usernameView.setText(username);

        // Fetch player data via repository
        repo = new ChessRepositoryJava();
        inFlight = repo.getCompletePlayerAsync(username)
                .thenAccept(player -> {
//                    Log.d(TAG, "onCreate: Constructed Player: " + player.toJSON());
                    if (isFinishing() || isDestroyed()) return;
                    runOnUiThread(() -> {
                        // Avatar (null/placeholder-safe)
                        URI avatarUri = player.getAvatarUrl();
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

                        String title = player.getTitle();
                        if (title != null && !title.isEmpty())
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inFlight != null) inFlight.cancel(true);
        if (repo != null) repo.close();
    }
}

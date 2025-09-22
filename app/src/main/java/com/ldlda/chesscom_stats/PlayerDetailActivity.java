package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class PlayerDetailActivity extends AppCompatActivity {
    private ChessRepositoryJava repo;
    private CompletableFuture<Void> inFlight;

    private String formatInstant(java.time.Instant instant) {
        if (instant == null) return "";
        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return formatter.format(date);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        ImageView avatar = findViewById(R.id.player_detail_avatar);
        TextView usernameView = findViewById(R.id.player_detail_username);
        TextView nameView = findViewById(R.id.player_detail_name);
        TextView statsView = findViewById(R.id.player_detail_stats);

        String username = getIntent().getStringExtra("username");
        assert username != null;

        usernameView.setText(username);

        // Fetch player data via repository (async, lifecycle-friendly)
        repo = new ChessRepositoryJava();
        inFlight = repo.getPlayerAsync(username)
                .thenAccept(player -> {
                    if (isFinishing() || isDestroyed()) return;
                    runOnUiThread(() -> {
                        // Avatar (null/placeholder-safe)
                        URI avatarUri = player.getProfilePictureResource();
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
                            stats.append("Title: ").append(title).append("\n");

                        // Build and set the base stats first
                        stats.append("Country: ").append("Loading…").append("\n");
                        statsView.setText(stats.toString());

                        {// Then kick off the async fetch
                            URI countryUri = player.getCountryUrl();
                            repo.getCountryByUrlAsync(countryUri) // returns CompletableFuture<CountryInfo>
                                    .thenAccept(info -> {
                                        if (isFinishing() || isDestroyed()) return;
                                        runOnUiThread(() -> {
                                            // Rebuild or patch the text; simplest: replace the placeholder
                                            String updated = statsView.getText().toString()
                                                    .replace("Country: Loading…", "Country: " + info.getName());
                                            statsView.setText(updated);
                                        });
                                    })
                                    .exceptionally(ex -> {
                                        // Optional: fallback to code-based Locale name if API fails
                                        runOnUiThread(() -> {
                                            String fallback = statsView.getText().toString()
                                                    .replace("Country: Loading…", "Country: Unknown");
                                            statsView.setText(fallback);
                                        });
                                        return null;
                                    });
                        }

                        Instant joined = player.getJoined();
                        stats.append("Joined: ").append(formatInstant(joined)).append("\n");

                        Instant lastOnline = player.getLastOnline();
                        stats.append("Last Online: ").append(formatInstant(lastOnline)).append("\n");

                        String status = player.getStatus();
                        if (!status.isEmpty()) stats.append("Status: ").append(status).append("\n");

                        statsView.setText(stats.toString());
                    });
                })
                .exceptionally(ex -> {
                    if (isFinishing() || isDestroyed()) return null;
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

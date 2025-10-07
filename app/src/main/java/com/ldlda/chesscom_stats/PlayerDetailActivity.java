package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ldlda.chesscom_stats.api.data.CountryInfo;
import com.ldlda.chesscom_stats.api.data.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.databinding.ActivityPlayerDetailBinding;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class PlayerDetailActivity extends AppCompatActivity {
    private final String TAG = "PlayerDetailActivity";
    private ChessRepoAdapterJava repo;
    private CompletableFuture<Void> inFlight;

    private ActivityPlayerDetailBinding binding;

    boolean isFavorited = false;
    private String username;
    private ImageView avatar;
    private TextView usernameView;
    private  TextView nameView;
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

        avatar = binding.playerDetailAvatar;
        usernameView = binding.playerDetailUsername;
        nameView = binding.playerDetailName;
        statsView = binding.playerDetailStats;

        addFavoriteBtn = binding.addToFavBtn;

        username = getIntent().getStringExtra("username");

        try (FileInputStream fis = openFileInput("favorites.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Toast.makeText(PlayerDetailActivity.this, line, Toast.LENGTH_SHORT).show();
                Toast.makeText(PlayerDetailActivity.this, username, Toast.LENGTH_SHORT).show();
                if (line.trim().equalsIgnoreCase(username)) {
                    isFavorited = true;
                    break;
                }
            }
        } catch (IOException ignored) {}

        addFavoriteBtn.setText(isFavorited? R.string.remove_fav : R.string.add_fav);

        addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorited)  // Remove fav
                {
                    try {
                        FileInputStream fis = openFileInput("favorites.txt");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                        List<String> favorites = new ArrayList<>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().equalsIgnoreCase(username)) {
                                favorites.add(line.trim());
                            }
                        }
                        reader.close();

                        FileOutputStream fos = openFileOutput("favorites.txt", MODE_PRIVATE);
                        for (String fav : favorites) {
                            fos.write((fav + "\n").getBytes());
                        }
                        fos.close();

                        isFavorited = false;
                        Toast.makeText(PlayerDetailActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    // Add fav
                    try (FileOutputStream fos = openFileOutput("favorites.txt", MODE_APPEND)) {
                        fos.write((username + "\n").getBytes());
                        isFavorited = true;
                        Toast.makeText(PlayerDetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                addFavoriteBtn.setText(isFavorited? R.string.remove_fav : R.string.add_fav);
            }
        });

        if (username == null) {
            throw new IllegalArgumentException("Intent extra 'username' must not be null");
        }

        usernameView.setText(username);

        // Fetch player data via repository
        fetchPlayerData();
    }

    private void fetchPlayerData(){
        repo = new ChessRepoAdapterJava();
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

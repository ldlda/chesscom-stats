package com.ldlda.chesscom_stats;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class PlayerDetailActivity extends AppCompatActivity {
    private String convertTimestamp(String timestamp) {
        try {
            long epochSeconds = Long.parseLong(timestamp);
            Date date = new Date(epochSeconds * 1000L); // Convert to milliseconds
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return formatter.format(date);
        } catch (NumberFormatException e) {
            return timestamp; // Return original if parsing fails
        }
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
        usernameView.setText(username);

        // Fetch player data from chess.com API
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL("https://api.chess.com/pub/player/" + username);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                JSONObject obj = new JSONObject(response.toString());
                String avatarUrl = obj.optString("avatar", null);
                String name = obj.optString("name", "");
                String title = obj.optString("title", "");
                String country = obj.optString("country", "");
                String joined = convertTimestamp(obj.optString("joined", ""));
                String lastOnline = convertTimestamp(obj.optString("last_online", ""));
                String status = obj.optString("status", "");
                runOnUiThread(() -> {
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Picasso.get().load(avatarUrl).placeholder(R.mipmap.ic_launcher).into(avatar);
                    }
                    nameView.setText(name.isEmpty() ? "No name provided" : name);
                    StringBuilder stats = new StringBuilder();
                    if (!title.isEmpty()) stats.append("Title: ").append(title).append("\n");
                    if (!country.isEmpty()) stats.append("Country: ").append(country).append("\n");
                    if (!joined.isEmpty()) stats.append("Joined: ").append(joined).append("\n");
                    if (!lastOnline.isEmpty()) stats.append("Last Online: ").append(lastOnline).append("\n");
                    if (!status.isEmpty()) stats.append("Status: ").append(status).append("\n");
                    statsView.setText(stats.toString());
                });
            } catch (Exception e) {
                runOnUiThread(() -> statsView.setText("Failed to load player data."));
            }
        });
    }
}

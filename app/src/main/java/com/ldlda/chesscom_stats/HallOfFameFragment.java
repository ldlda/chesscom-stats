package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.ldlda.chesscom_stats.adapter.HallofFameAdapter;
import com.ldlda.chesscom_stats.PlayerDetailActivity;

public class HallOfFameFragment extends Fragment {
    private RecyclerView recyclerView;
    private HallofFameAdapter adapter;
    private final List<HallofFameAdapter.Player> playerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
        recyclerView = view.findViewById(R.id.hall_of_fame_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HallofFameAdapter(playerList, player -> {
            // Launch PlayerDetailActivity with the selected username
            android.content.Intent intent = new android.content.Intent(getContext(), PlayerDetailActivity.class);
            intent.putExtra("username", player.username);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        fetchTopPlayers();
        return view;
    }

    private void fetchTopPlayers() {
        String url = "https://api.chess.com/pub/leaderboards";
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray blitz = response.getJSONArray("live_blitz");
                        playerList.clear();
                        for (int i = 0; i < Math.min(10, blitz.length()); i++) {
                            JSONObject playerObj = blitz.getJSONObject(i);
                            String username = playerObj.getString("username");
                            String avatar = playerObj.optString("avatar", "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif");
                            int rating = playerObj.getInt("score");
                            playerList.add(new HallofFameAdapter.Player(username, avatar, rating));
                        }
                        adapter.updatePlayers(playerList);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Failed to parse leaderboard", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to fetch leaderboard", Toast.LENGTH_SHORT).show();
                    android.util.Log.e("HallOfFameFragment", "Volley error: " + error.toString());
                }
        );
        queue.add(request);
    }
}

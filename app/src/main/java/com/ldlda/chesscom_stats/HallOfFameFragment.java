package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ldlda.chesscom_stats.adapter.HallofFameAdapter;
import com.ldlda.chesscom_stats.PlayerDetailActivity;
import com.ldlda.chesscom_stats.api.fetch.ChessApi;
import com.ldlda.chesscom_stats.api.data.LeaderboardResponse;
import com.ldlda.chesscom_stats.api.data.PlayerLeaderboardEntry;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

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
        new AsyncTask<Void, Void, List<HallofFameAdapter.Player>>() {
            private Exception error;
            @Override
            protected List<HallofFameAdapter.Player> doInBackground(Void... voids) {
                List<HallofFameAdapter.Player> result = new ArrayList<>();
                try {
                    LeaderboardResponse leaderboard = ChessApi.getLeaderboards();
                    List<PlayerLeaderboardEntry> blitz = leaderboard.getLiveBlitz();
                    for (int i = 0; i < Math.min(10, blitz.size()); i++) {
                        PlayerLeaderboardEntry entry = blitz.get(i);
                        String username = entry.getUsername();
                        String avatar = entry.getAvatar() != null ? entry.getAvatar() : "https://www.chess.com/bundles/web/images/noavatar_l.84a92436.gif";
                        int rating = entry.getScore();
                        result.add(new HallofFameAdapter.Player(username, avatar, rating));
                    }
                } catch (Exception e) {
                    error = e;
                }
                return result;
            }
            @Override
            protected void onPostExecute(List<HallofFameAdapter.Player> players) {
                if (error != null) {
                    Toast.makeText(getContext(), "Failed to fetch leaderboard", Toast.LENGTH_SHORT).show();
                    android.util.Log.e("HallOfFameFragment", "API error: " + error.toString());
                } else {
                    playerList.clear();
                    playerList.addAll(players);
                    adapter.updatePlayers(playerList);
                }
            }
        }.execute();
    }
}

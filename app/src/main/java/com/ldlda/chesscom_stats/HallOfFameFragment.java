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
import com.ldlda.chesscom_stats.api.data.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.data.Leaderboards;
import com.ldlda.chesscom_stats.api.fetch.DefaultChessApi;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

public class HallOfFameFragment extends Fragment {
    private RecyclerView recyclerView;
    private HallofFameAdapter adapter;
    private final List<LeaderboardEntry> playerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
        recyclerView = view.findViewById(R.id.hall_of_fame_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HallofFameAdapter(playerList, player -> {
            // Launch PlayerDetailActivity with the selected username
            android.content.Intent intent = new android.content.Intent(getContext(), PlayerDetailActivity.class);
            intent.putExtra("username", player.getUsername());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        fetchTopPlayers();
        return view;
    }
    private final AsyncTask<Void, Void, List<LeaderboardEntry>> asyncTask = new AsyncTask<Void, Void, List<LeaderboardEntry>>() {
        private Exception error;
        @Override
        protected List<LeaderboardEntry> doInBackground(Void... voids) {
            List<LeaderboardEntry> result = new ArrayList<>();
            try {
                Leaderboards leaderboard = DefaultChessApi.INSTANCE.getLeaderboardsSync();
                List<LeaderboardEntry> blitz = leaderboard.getBlitz();
                for (int i = 0; i < Math.min(10, blitz.size()); i++) {
                    LeaderboardEntry entry = blitz.get(i);
                    result.add(entry);
                }
            } catch (Exception e) {
                error = e;
            }
            return result;
        }
        @Override
        protected void onPostExecute(List<LeaderboardEntry> players) {
            if (error != null) {
                Toast.makeText(getContext(), "Failed to fetch leaderboard", Toast.LENGTH_SHORT).show();
                android.util.Log.e("HallOfFameFragment", "API error: " + error.toString());
            } else {
                playerList.clear();
                playerList.addAll(players);
                adapter.updatePlayers(playerList);
            }
        }
    };
    private void fetchTopPlayers() {
        asyncTask.execute();
    }
}

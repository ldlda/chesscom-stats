package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.adapter.HallOfFameAdapter;
import com.ldlda.chesscom_stats.api.data.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava;

import java.util.ArrayList;
import java.util.List;

public class HallOfFameFragment extends Fragment {
    private HallOfFameAdapter adapter;
    private ChessRepositoryJava repo;
    private SearchView searchView;
    private List<LeaderboardEntry> allPlayers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
        // RecyclerView handler
        RecyclerView recyclerView = view.findViewById(R.id.hall_of_fame_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HallOfFameAdapter(player -> {
            // Launch PlayerDetailActivity with the selected username
            android.content.Intent intent = new android.content.Intent(getContext(), PlayerDetailActivity.class);
            intent.putExtra("username", player.getUsername());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        repo = new ChessRepositoryJava();
        fetchTopPlayers();

        // SearchView handler
        searchView = view.findViewById(R.id.player_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<LeaderboardEntry> filtered = new ArrayList<>();
                for (LeaderboardEntry player : allPlayers) {
                    if (player.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                        filtered.add(player);
                    }
                }
                adapter.submitList(filtered);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        return view;
    }

    private void fetchTopPlayers() {
        repo.getLeaderboardsAsync().thenAccept(boards -> {
            List<LeaderboardEntry> blitz = boards.getBlitz();

            //List<LeaderboardEntry> top = blitz.subList(0, Math.min(20, blitz.size())); // top 20 out of how many

            // A new safe copy list from the blitz list
            allPlayers = new ArrayList<>(blitz);
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() ->
                    adapter.submitList(new ArrayList<>(allPlayers))
            );
        }).exceptionally(ex -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), R.string.failed_to_fetch_leaderboard, Toast.LENGTH_SHORT).show()
                );
            }
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (repo != null) repo.close();
    }
}

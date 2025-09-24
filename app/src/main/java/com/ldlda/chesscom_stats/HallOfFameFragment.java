package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
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
        return view;
    }

    private void fetchTopPlayers() {
        repo.getLeaderboardsAsync().thenAccept(boards -> {
            List<LeaderboardEntry> blitz = boards.getBlitz();
            List<LeaderboardEntry> top = blitz.subList(0, Math.min(20, blitz.size())); // top 20 out of how many
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() ->
                    adapter.submitList(new ArrayList<>(top))
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

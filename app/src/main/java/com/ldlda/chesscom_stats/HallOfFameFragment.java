package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ldlda.chesscom_stats.adapter.HallOfFameAdapter;
import com.ldlda.chesscom_stats.api.data.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava;
import com.ldlda.chesscom_stats.databinding.FragmentHallOfFameBinding;

import java.util.ArrayList;
import java.util.List;

public class HallOfFameFragment extends Fragment {
    private static final String TAG = "HallOfFameFragment";
    private static final long MIN_REFRESH_INTERVAL_MS = 5_000; // 5 seconds between forced refreshes
    private long lastRefreshAt = 0L;
    private HallOfFameAdapter adapter;
    private ChessRepositoryJava repo;

    private FragmentHallOfFameBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHallOfFameBinding.inflate(inflater, container, false);
        SwipeRefreshLayout swipeRefreshLayout = binding.leaderboardSwipeRefresh;
        RecyclerView recyclerView = binding.hallOfFameRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HallOfFameAdapter(player -> {
            // Launch PlayerDetailActivity with the selected username
            android.content.Intent intent = new android.content.Intent(getContext(), PlayerDetailActivity.class);
            intent.putExtra("username", player.getUsername());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        repo = new ChessRepositoryJava();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            long now = System.currentTimeMillis();
            if (now - lastRefreshAt < MIN_REFRESH_INTERVAL_MS) {
                // Too soon; just cancel the spinner quickly.
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            fetchTopPlayers(swipeRefreshLayout);
        });
        // Initial load (from cache if present, otherwise network)
        fetchTopPlayers(swipeRefreshLayout);
        Log.d(TAG, "onCreateView: called fetchTopPlayers");
        return binding.getRoot();
    }

    private void fetchTopPlayers(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        Log.d(TAG, "fetchTopPlayers: reloading");
        repo.getLeaderboardsAsync()
                .thenAccept(boards -> {
                    List<LeaderboardEntry> blitz = boards.getBlitz();
                    List<LeaderboardEntry> top = blitz.subList(0, Math.min(20, blitz.size())); // top 20 out of how many
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() ->
                            {
                                lastRefreshAt = System.currentTimeMillis();
                                Log.d(TAG, "fetchTopPlayers: reloaded / added to view");
                                adapter.submitList(new ArrayList<>(top));
                                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                            }
                    );
                }).exceptionally(ex -> {
                    Log.e(TAG, "fetchTopPlayers: getLeaderboards failed", ex);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                {
                                    Toast.makeText(getContext(), R.string.failed_to_fetch_leaderboard, Toast.LENGTH_SHORT).show();
                                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                                }
                        );
                    }
                    return null;
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (repo != null) repo.close();
        binding = null;
    }
}

package com.ldlda.chesscom_stats.ui.leaderboards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.LeaderboardEntry;
import com.ldlda.chesscom_stats.databinding.FragmentHallOfFameBinding;
import com.ldlda.chesscom_stats.ui.playerdetail.PlayerDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardsFragment extends Fragment {
    private static final String TAG = "LeaderboardsFragment";
    private static final long MIN_REFRESH_INTERVAL_MS = 10_000; // 10 seconds between forced refreshes
    private long lastRefreshAt = 0L;
    private LeaderboardsAdapter adapter;
    private LeaderboardsViewModel viewModel;
    private SearchView searchView;
    private List<LeaderboardEntry> allPlayers = new ArrayList<>();

    private FragmentHallOfFameBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHallOfFameBinding.inflate(inflater, container, false);
        SwipeRefreshLayout swipeRefreshLayout = binding.leaderboardSwipeRefresh;
        RecyclerView recyclerView = binding.hallOfFameRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardsAdapter(player -> {
            // Navigate to PlayerDetailFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, PlayerDetailFragment.newInstance(player.getUsername()))
                    .addToBackStack("playerDetail")
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this.requireActivity()).get(LeaderboardsViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            long now = System.currentTimeMillis();
            if (now - lastRefreshAt < MIN_REFRESH_INTERVAL_MS) {
                // Too soon; just cancel the spinner quickly.
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), R.string.refresh_too_soon, Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.load(true);
        });

        // Initial load (from cache if present, otherwise network)
        viewModel.load(false);

        // SearchView handler
        searchView = binding.playerSearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                List<LeaderboardEntry> filtered = allPlayers
                        .stream()
                        .filter(player -> player.getUsername().toLowerCase()
                                .contains(newText.toLowerCase()))
                        .toList();
                adapter.submitList(filtered);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        // Observe
        viewModel.getLoading().observe(getViewLifecycleOwner(), swipeRefreshLayout::setRefreshing);
        viewModel.getData().observe(getViewLifecycleOwner(), boards -> {
            List<com.ldlda.chesscom_stats.api.data.LeaderboardEntry> blitz = boards.getBlitz();
            allPlayers = new ArrayList<>(blitz);
            adapter.submitList(new ArrayList<>(allPlayers));
            lastRefreshAt = System.currentTimeMillis();
        });
        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && getContext() != null) {
                Toast.makeText(getContext(), R.string.failed_to_fetch_leaderboard, Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

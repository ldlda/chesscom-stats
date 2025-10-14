package com.ldlda.chesscom_stats.ui.leaderboards;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry;
import com.ldlda.chesscom_stats.databinding.FragmentHallOfFameBinding;
import com.ldlda.chesscom_stats.ui.playerdetail.PlayerDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

public class LeaderboardsFragment extends Fragment {
    private static final String TAG = "LeaderboardsFragment";
    private static final long MIN_REFRESH_INTERVAL_MS = 10_000; // 10 seconds between forced refreshes
    private long lastRefreshAt = 0L;
    private LeaderboardsAdapter adapter;
    private LeaderboardsViewModel viewModel;
    private SearchView searchView;
    private List<LeaderboardEntry> allPlayers = new ArrayList<>();

    private FragmentHallOfFameBinding binding;

    // Podium views
    private LinearLayout podiumFirst, podiumSecond, podiumThird;
    private ImageView avatarFirst, avatarSecond, avatarThird;
    private TextView usernameFirst, usernameSecond, usernameThird;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHallOfFameBinding.inflate(inflater, container, false);

        // Bind podium views
        podiumFirst = binding.podiumFirst;
        podiumSecond = binding.podiumSecond;
        podiumThird = binding.podiumThird;
        avatarFirst = binding.avatarFirst;
        avatarSecond = binding.avatarSecond;
        avatarThird = binding.avatarThird;
        usernameFirst = binding.usernameFirst;
        usernameSecond = binding.usernameSecond;
        usernameThird = binding.usernameThird;

        SwipeRefreshLayout swipeRefreshLayout = binding.leaderboardSwipeRefresh;
        RecyclerView recyclerView = binding.hallOfFameRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardsAdapter(player -> {
            // Launch PlayerDetailActivity with LeaderboardEntry (fast path - no need to refetch data)
            Intent intent = new Intent(getContext(), PlayerDetailActivity.class);
            intent.putExtra(PlayerDetailActivity.EXTRA_PLAYER_ENTRY, player);
            // TODO: Pass actual timeclass when we support switching between blitz/bullet/rapid/daily
            // For now, hardcode "blitz" since that's what we fetch
            intent.putExtra(PlayerDetailActivity.EXTRA_TIMECLASS, "blitz");
            startActivity(intent);
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
                if (newText.isBlank()) {
                    List<LeaderboardEntry> rest = allPlayers.size() > 3
                            ? allPlayers.subList(3, allPlayers.size())
                            : new ArrayList<>();
                    adapter.submitList(new ArrayList<>(rest));
                } else {
                    List<LeaderboardEntry> filtered = allPlayers
                            .stream()
                            .filter(player -> player.getUsername().toLowerCase()
                                    .contains(newText.toLowerCase()))
                            .toList();
                    adapter.submitList(filtered);
                }
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
            List<LeaderboardEntry> blitz = boards.getBlitz();
            allPlayers = new ArrayList<>(blitz);

            // Update podium (top 3)
            if (allPlayers.size() >= 3) {
                updatePodium(allPlayers.get(0), allPlayers.get(1), allPlayers.get(2));
            }

            // Show rest in RecyclerView (skip top 3)
            List<LeaderboardEntry> rest = allPlayers.size() > 3
                    ? allPlayers.subList(3, allPlayers.size())
                    : new ArrayList<>();
            adapter.submitList(new ArrayList<>(rest));
            lastRefreshAt = System.currentTimeMillis();
        });
        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && getContext() != null) {
                Log.e(TAG, "onCreateView: fetch error", err);
                Toast.makeText(getContext(), R.string.failed_to_fetch_leaderboard, Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void updatePodium(LeaderboardEntry first, LeaderboardEntry second, LeaderboardEntry third) {
        // First place
        usernameFirst.setText(first.getUsername());
        loadAvatar(avatarFirst, first.getAvatarUrl());
        podiumFirst.setOnClickListener(v -> launchPlayerDetail(first));

        // Second place
        usernameSecond.setText(second.getUsername());
        loadAvatar(avatarSecond, second.getAvatarUrl());
        podiumSecond.setOnClickListener(v -> launchPlayerDetail(second));

        // Third place
        usernameThird.setText(third.getUsername());
        loadAvatar(avatarThird, third.getAvatarUrl());
        podiumThird.setOnClickListener(v -> launchPlayerDetail(third));
    }

    private void loadAvatar(ImageView imageView, HttpUrl avatarUrl) {
        if (avatarUrl != null) {
            Picasso.get()
                    .load(avatarUrl.toString())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }
    }

    private void launchPlayerDetail(LeaderboardEntry player) {
        Intent intent = new Intent(getContext(), PlayerDetailActivity.class);
        intent.putExtra(PlayerDetailActivity.EXTRA_PLAYER_ENTRY, player);
        intent.putExtra(PlayerDetailActivity.EXTRA_TIMECLASS, "blitz");
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

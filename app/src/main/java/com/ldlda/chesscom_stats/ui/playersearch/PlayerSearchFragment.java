package com.ldlda.chesscom_stats.ui.playersearch;

import static androidx.lifecycle.LifecycleKt.getCoroutineScope;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepository;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;
import com.ldlda.chesscom_stats.databinding.FragmentBoringBinding;
import com.ldlda.chesscom_stats.ui.playerdetail.PlayerDetailActivity;

public class PlayerSearchFragment extends Fragment {
    public static final String TAG = "PlayerSearchFragment";
    private static final long DEBOUNCE_DELAY = 1000L; // Wait that many milliseconds after user stops typing
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private FragmentBoringBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter adapter;
    private Runnable searchRunnable;
    private ChessRepoAdapterJava<ChessRepository> repo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = ChessRepoAdapterJava.getAdapterJava(new ChessRepositoryTimedCache(), getCoroutineScope(getLifecycle()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoringBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        binding.playerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // Cancel any pending search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Only search if we have at least 3 characters
                if (newText.length() >= 3) {
                    String username = newText.toLowerCase().trim();

                    // Schedule new search after delay
                    searchRunnable = () -> {
                        Log.i(TAG, "Searching for: " + username);
                        viewModel.load(username);
                    };
                    searchHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Cancel pending search and execute immediately
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                if (query.length() >= 3) {
                    String username = query.toLowerCase().trim();
                    Log.i(TAG, "Searching for: " + username);
                    viewModel.load(username);
                }
                return true;
            }

        });

        binding.playerSearchView.setIconified(false);
        binding.playerSearchView.requestFocus();

        // Setup adapter and RecyclerView
        adapter = new SearchAdapter(player -> {
            Intent intent = new Intent(getContext(), PlayerDetailActivity.class);
            // Pass username for SLOW PATH - activity will fetch all data from API
            intent.putExtra(PlayerDetailActivity.EXTRA_USERNAME, player.getUserView().getUsername());
            startActivity(intent);
        });

        binding.resultsRecycler.setAdapter(adapter);
        binding.resultsRecycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));

        // Observe search results and update adapter
        viewModel.getData().observe(getViewLifecycleOwner(), searchItems -> adapter.submitList(searchItems));
        viewModel.getError().observe(getViewLifecycleOwner(), e -> Log.e(TAG, "onCreateView:", e));
        // Force keyboard to appear
        getContext();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.playerSearchView, InputMethodManager.SHOW_IMPLICIT);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel any pending searches
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repo.close();
        repo = null;

    }
}
package com.ldlda.chesscom_stats.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.databinding.FragmentFavoritesBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private final List<FavoritesAdapter.FavoritePlayer> favs = new ArrayList<>(); // Ensure this is populated elsewhere
    private FavoritesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        RecyclerView recycler = binding.favRecycler;
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new FavoritesAdapter(new FavoritesAdapter.OnFavoriteClickListener() {
            @Override
            public void onRemoveClicked(String username) {
                int pos = -1;
                for (int i = 0; i < favs.size(); i++) {
                    if (favs.get(i).username.equals(username)) {
                        pos = i;
                        break;
                    }
                }
                Log.i("FavoritesFragment", "Removing favorite: " + username + " at position " + pos);
                if (pos != -1) {
                    // Animate out before removing
                    var viewHolder = binding.favRecycler.findViewHolderForAdapterPosition(pos);
                    if (viewHolder == null) return;
                    View view = viewHolder.itemView;
                    view.animate()
                            .alpha(0f)
                            .translationX(view.getWidth())
                            .setDuration(300)
                            .withEndAction(() -> {
                                // Remove via ViewModel (background thread)
                                viewModel.removeFavoriteByUsername(username);
                            })
                            .start();
                }
            }

            @Override
            public void onItemClicked(String username) {
                Toast.makeText(requireContext(), "Nice choice liking " + username, Toast.LENGTH_SHORT).show();
            }
        });

        recycler.setAdapter(adapter);

        // Observe favorite users from ViewModel
        viewModel.getFavoriteUsers().observe(getViewLifecycleOwner(), users -> {
            List<FavoritesAdapter.FavoritePlayer> favoritePlayers = users.stream()
                .map(user -> new FavoritesAdapter.FavoritePlayer(
                    user.getUsername(),
                    user.getTitle() != null ? user.getTitle() : "",
                    user.getLastOnlDate() != null ? user.getLastOnlDate() : "",
                    0 // Using a boilerplate value for rating
                ))
                .collect(Collectors.toList());

            favs.clear();
            favs.addAll(favoritePlayers);
            adapter.submitList(new ArrayList<>(favoritePlayers));
        });

        // Load favorites on creation
        viewModel.loadFavorites();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload favorites when returning to this screen
        viewModel.loadFavorites();
        binding.favRecycler.scheduleLayoutAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

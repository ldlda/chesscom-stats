package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.adapter.FavoritesAdapter;
import com.ldlda.chesscom_stats.adapter.HallOfFameAdapter;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.databinding.FragmentFavoritesBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FavoritesFragment extends Fragment {
    private static final String TAG = "FavoritesFragment";
    private FragmentFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private Set<String> favs = new HashSet<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        RecyclerView recycler = binding.favRecycler;
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        favs = loadFavorites();
        List<String> favList = new ArrayList<>(favs);

        adapter = new FavoritesAdapter(favList,new FavoritesAdapter.OnFavoriteClickListener() {
            @Override
            public void onRemoveClicked(String username) {
                favs.remove(username);
                saveFavorites(favs);
                adapter.updateData(new ArrayList<>(favs));
            }

            @Override
            public void onItemClicked(String username) {
                Toast.makeText(requireContext(), "Nice choice liking " + username, Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);

        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null){
            Set<String> favs = loadFavorites();
            adapter.updateData(new ArrayList<>(favs));
        }
    }

    private File getFavoritesFile() {
        return new File(requireContext().getFilesDir(), "favorites.txt");
    }
    private Set<String> loadFavorites() {
        Set<String> favs = new HashSet<>();
        File file = getFavoritesFile();
        if (!file.exists()) return favs;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                favs.add(line.trim());
            }
        } catch (Exception e) {
            Log.e("Favorites", "Error loading favorites: " + e.getMessage());
        }
        return favs;
    }

    private void saveFavorites(Set<String> favs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFavoritesFile(), false))) {
            for (String name : favs) writer.write(name + "\n");
        } catch (Exception e) {
            Log.e("Favorites", "Error saving favorites: " + e.getMessage());
        }
    }

}


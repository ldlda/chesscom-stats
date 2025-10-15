package com.ldlda.chesscom_stats.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.databinding.FragmentHomeBinding;

import java.util.Locale;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Player Search card - Navigate with Navigation Component
        CardView plr_search_btn = binding.plrSearcher;
        plr_search_btn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_playerSearch));

        // Club Finder card - Navigate with Navigation Component
        CardView club_finder = binding.clubFinder;
        club_finder.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_clubs));

        // FIDE Prediction card - Navigate with Navigation Component
        CardView predict_fide = binding.fidePrediction;
        predict_fide.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_fidePredict));

        // Puzzle card - Navigate with Navigation Component
        CardView puzzle = binding.puzzle;
        puzzle.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_home_to_puzzle));

        // Language switcher - Popup menu (stays same)
        CardView changeLang = binding.changeLang;
        changeLang.setOnClickListener(this::showLanguageMenu);

        return binding.getRoot();
    }

    private void showLanguageMenu(View anchor) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.language, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.lang_en) {
                setLocale("en");
                return true;
            } else if (itemId == R.id.lang_fr) {
                setLocale("fr");
                return true;
            } else if (itemId == R.id.lang_vi) {
                setLocale("vi");
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(
                config, requireContext().getResources().getDisplayMetrics()
        );

        // Optionally reload fragment or activity
        requireActivity().recreate();
    }
}

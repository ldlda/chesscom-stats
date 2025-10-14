package com.ldlda.chesscom_stats;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class HomeFragment extends Fragment {
    //Inflate fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        CardView plr_search_btn = view.findViewById(R.id.plr_searcher);

        plr_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);

                PlayerSearchFragment searchFragment = new PlayerSearchFragment();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, searchFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView club_finder = view.findViewById(R.id.club_finder);
        club_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);

                ClubFragment clubFragment = new ClubFragment();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, clubFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView predict_fide = view.findViewById(R.id.fide_prediction);
        predict_fide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);

                FidePredictFragment fidePredictFragment = new FidePredictFragment();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fidePredictFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView puzzle = view.findViewById(R.id.puzzle);
        puzzle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);

                PuzzleFragment puzzleFragment = new PuzzleFragment();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, puzzleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView changeLang = view.findViewById(R.id.change_lang);

        changeLang.setOnClickListener(v -> {
            showLanguageMenu(v);
        });

        return view;
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

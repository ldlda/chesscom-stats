package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    //Inflate fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button plr_search_btn = view.findViewById(R.id.plr_searcher);

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

        Button club_finder = view.findViewById(R.id.club_finder);
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

        Button predict_fide = view.findViewById(R.id.fide_prediction);
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
        return view;
    }
}

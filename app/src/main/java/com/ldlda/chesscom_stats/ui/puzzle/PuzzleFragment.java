package com.ldlda.chesscom_stats.ui.puzzle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.PlayerStatsData;
import com.ldlda.chesscom_stats.java_api.Puzzle;
import com.ldlda.chesscom_stats.java_api.PuzzleData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PuzzleFragment extends Fragment {
    private ProgressBar progressBar;
    private MaterialButton solveURI;
    private ImageView puzzleView;
    private TextView puzzleName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_puzzle, container, false);
        progressBar = view.findViewById(R.id.prog_bar);
        solveURI = view.findViewById(R.id.puzzleURI);
        puzzleView = view.findViewById(R.id.puzzleView);
        puzzleName = view.findViewById(R.id.puzzle_name);

        Puzzle puzzleApi = ApiClient.getClient().create(Puzzle.class);

        Call<PuzzleData> statCall = puzzleApi.getDailyPuzzleData();

        progressBar.setVisibility(View.VISIBLE);
        statCall.enqueue(new Callback<PuzzleData>() {
            @Override
            public void onResponse(Call<PuzzleData> call, Response<PuzzleData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    PuzzleData puzzle = response.body();
                    puzzleName.setText(puzzle.title);
                    Glide.with(requireContext())
                            .load(puzzle.image)
                            .into(puzzleView);
                }else{
                    Toast.makeText(requireContext(),"Error getting puzzle-Response code:"+ response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PuzzleData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(),"Error getting puzzle: "+ t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}
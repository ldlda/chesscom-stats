package com.ldlda.chesscom_stats.ui.puzzle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "PuzzleFragment";
    private ProgressBar progressBar;
    private MaterialButton solveURI;
    private ImageView puzzleView;
    private TextView puzzleName;

    private String puzzleUrl;

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
                    puzzleUrl = puzzle.url;

                    Glide.with(requireContext())
                            .load(puzzle.image)
                            .into(puzzleView);
                }else{
                    Log.e(TAG,"Puzzle load error-Response code:"+response.code());
                    Toast.makeText(requireContext(),"Error loading puzzle-Response code:"+ response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PuzzleData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG,t.getMessage());
                Toast.makeText(requireContext(),"Error getting puzzle: "+ t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        solveURI.setOnClickListener(v -> {
            if (puzzleUrl != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(puzzleUrl));
                startActivity(intent);
                /*
                Bundle bundle = new Bundle();
                bundle.putString("puzzle_url", puzzleUrl);

                PuzzleSolve webFragment = new PuzzleSolve();
                webFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,  // animation for the new fragment entering
                                R.anim.slide_out_left,      // animation for the current fragment exiting
                                R.anim.slide_in_left,   // animation when coming back (pop enter)
                                R.anim.slide_out_right      // animation when going back (pop exit)
                        )
                        .replace(R.id.fragment_container, webFragment)
                        .addToBackStack(null)
                        .commit();
                 */
            } else {
                Toast.makeText(requireContext(), "Puzzle not loaded yet", Toast.LENGTH_SHORT).show();
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
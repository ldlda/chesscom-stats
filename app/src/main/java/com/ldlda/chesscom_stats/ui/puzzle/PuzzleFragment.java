package com.ldlda.chesscom_stats.ui.puzzle;

import static androidx.lifecycle.LifecycleKt.getCoroutineScope;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepository;
import com.ldlda.chesscom_stats.databinding.FragmentPuzzleBinding;
import com.ldlda.chesscom_stats.di.RepoProvider;

import org.jetbrains.annotations.NotNull;

import retrofit2.HttpException;

public class PuzzleFragment extends Fragment {
    private static final String TAG = "PuzzleFragment";
    private ProgressBar progressBar;
    private MaterialButton solveURI;
    private ImageView puzzleView;
    private TextView puzzleName;

    private String puzzleUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPuzzleBinding binding = FragmentPuzzleBinding.inflate(inflater, container, false);
        progressBar = binding.progBar;
        solveURI = binding.puzzleURI;
        puzzleView = binding.puzzleView;
        puzzleName = binding.puzzleName;

        ChessRepoAdapterJava<@NotNull ChessRepository> repo = RepoProvider.defaultRepository(requireContext()).buildJavaAdapter(getCoroutineScope(getLifecycle()));

        progressBar.setVisibility(View.VISIBLE);

        repo.getDailyPuzzleAsync().thenAccept(
                puzzle -> {

                    puzzleName.setText(puzzle.getTitle());
                    puzzleUrl = puzzle.getUrl().toString();
                    String l = puzzle.getImage()
                            .newBuilder()
                            .removeAllQueryParameters("size")
                            .setQueryParameter("size", "3")
                            .build().toString();
                    Log.d(TAG, "onCreateView: " + l);
                    Glide.with(requireContext())
                            .load(l)
                            .placeholder(R.drawable.ic_pawn)
                            .error(R.drawable.ic_banned_acc)
                            .into(puzzleView);
                }
        ).exceptionally(
                throwable -> {
                    if (throwable.getCause() instanceof HttpException he) {
                        Log.e(TAG, "Puzzle load error-Response code:" + he.code());
                        Toast.makeText(requireContext(), "Error loading puzzle-Response code:" + he.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "shit:" + throwable.getMessage());
                        Toast.makeText(requireContext(), "Error getting puzzle: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
        ).handle((ignored, ignored1) -> {
            progressBar.setVisibility(View.GONE);
            return null;
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


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}
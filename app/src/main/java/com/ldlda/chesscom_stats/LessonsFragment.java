package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LessonsFragment extends Fragment {
    public LessonsFragment() {
        // Required empty public constructor
    }

    public static LessonsFragment newInstance() {
        return new LessonsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        // Array of lesson ImageView IDs
        int[] lessonIds = {
                R.id.introduction,
                R.id.lesson_one,
                R.id.lesson_two,
                R.id.lesson_three,
                R.id.lesson_four,
                R.id.lesson_five
        };

        // Loop through each ImageView and set a click listener
        for (int i = 0; i < lessonIds.length; i++) {
            final int lessonIndex = i; // capture index
            ImageView lessonView = view.findViewById(lessonIds[i]);
            lessonView.setOnClickListener(v -> openLesson(lessonIndex));
        }

        return view;
    }

    /**
     * Opens a ContentLesson fragment with the given lesson index.
     */
    private void openLesson(int lessonIndex) {
        ContentLesson contentLesson = ContentLesson.newInstance(lessonIndex);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main, contentLesson, "ContentLesson")
                .addToBackStack(null)
                .commit();
    }
}

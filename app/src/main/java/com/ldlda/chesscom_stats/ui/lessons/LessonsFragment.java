package com.ldlda.chesscom_stats.ui.lessons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;

import java.util.Arrays;
import java.util.List;

public class LessonsFragment extends Fragment {

    private RecyclerView recyclerView;

    public LessonsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> lessonTitles = Arrays.asList(
                "Lesson 1:",
                "Lesson 2:",
                "Lesson 3:",
                "Lesson 4:",
                "Lesson 5:"
        );

        LessonAdapter adapter = new LessonAdapter(lessonTitles, this::openLesson);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void openLesson(int lessonIndex) {
        LessonContents contentLesson = LessonContents.newInstance(lessonIndex);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(this)
                .add(R.id.fragment_container, contentLesson, "ContentLesson")
                .addToBackStack("gay")
                .commit();
    }
}

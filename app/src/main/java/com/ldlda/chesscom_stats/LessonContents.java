package com.ldlda.chesscom_stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class LessonContents extends Fragment {

    private TextView titleView, contentView;

    // Sample lesson titles
    private final List<String> lessonTitles = Arrays.asList(
            "Lesson 1:",
            "Lesson 2:",
            "Lesson 3:",
            "Lesson 4:",
            "Lesson 5:"
    );

    // Sample lesson contents (short draft)
    private final List<String> lessonContents = Arrays.asList(
            "Draft content for lesson 1...",
            "Draft content for lesson 2...",
            "Draft content for lesson 3...",
            "Draft content for lesson 4...",
            "Draft content for lesson 5..."
    );

    public static LessonContents newInstance(int lessonIndex) {
        LessonContents fragment = new LessonContents();
        Bundle args = new Bundle();
        args.putInt("lesson_index", lessonIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_contents, container, false);

        titleView = view.findViewById(R.id.ls_title);
        contentView = view.findViewById(R.id.ls_content);

        int lessonIndex = getArguments() != null ? getArguments().getInt("lesson_index") : 0;

        // Set correct lesson title + content with bounds checking
        if (lessonIndex >= 0 && lessonIndex < lessonTitles.size() && lessonIndex < lessonContents.size()) {
            titleView.setText(lessonTitles.get(lessonIndex));
            contentView.setText(lessonContents.get(lessonIndex));
        } else {
            titleView.setText("Invalid lesson");
            contentView.setText("The requested lesson does not exist.");
        }

        return view;
    }
}

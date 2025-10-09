package com.ldlda.chesscom_stats;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ldlda.chesscom_stats.R;

import java.util.List;
import java.util.ArrayList;


public class LessonContents extends Fragment {

    private TextView detailDesc, detailTitle;
    private ImageView detailImage;
    private Button btnPrevious, btnNext;
    private List<Lesson> lessonsList;
    private int currentIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_contents, container, false);

        detailDesc = view.findViewById(R.id.detailDesc);
        detailTitle = view.findViewById(R.id.detailTitle);
        detailImage = view.findViewById(R.id.detailImage);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);


        Bundle args = getArguments();
        applyArgsToViews(args);
        setupLessonList();

        // 🔹 Find current index
        if (args != null && args.containsKey("Title")) {
            String title = args.getString("Title");
            for (int i = 0; i < lessonsList.size(); i++) {
                if (lessonsList.get(i).getDataTitle().equals(title)) {
                    currentIndex = i;
                    break;
                }
            }
        }

        updateButtonState();

        //Next button
        btnNext.setOnClickListener(v -> {
            if (currentIndex < lessonsList.size() - 1) {
                currentIndex++;
                showLesson(currentIndex);
            }
        });

        //Previous button
        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showLesson(currentIndex);
            }
        });

        return view;
    }

    private void setupLessonList() {
        lessonsList = new ArrayList<>();
        lessonsList.add(new Lesson("Lesson 1", R.string.about, "Beginner", R.drawable.list_detail));
        lessonsList.add(new Lesson("Lesson 2", R.string.about, "Beginner", R.drawable.list_detail));
        lessonsList.add(new Lesson("Lesson 3", R.string.about, "Intermidiate", R.drawable.list_detail));
        lessonsList.add(new Lesson("Lesson 4", R.string.about, "Advance", R.drawable.list_detail));
        lessonsList.add(new Lesson("Lesson 5", R.string.about, "Advance", R.drawable.list_detail));
    }

    private void showLesson(int index) {

        View root = getView();
        int enter;
        int exit;
        if(index < 0 || index >= lessonsList.size()) return;
        if(root == null) return;
        if(index > currentIndex){
            enter = R.anim.slide_in_right;
            exit = R.anim.slide_out_left;
        }
        else{
            enter = R.anim.slide_in_left;
            exit = R.anim.slide_out_right;
        }
        root.startAnimation(AnimationUtils.loadAnimation((requireContext()), exit));
        root.postDelayed(() -> {
            // Apply new content
            Lesson lesson = lessonsList.get(index);
            Bundle args = new Bundle();
            args.putString("Title", lesson.getDataTitle());
            args.putInt("Desc", lesson.getDataDesc());
            args.putInt("Image", lesson.getDataImage());
            applyArgsToViews(args);

            // Animate in new content
            root.startAnimation(AnimationUtils.loadAnimation(requireContext(), enter));
            currentIndex = index;
            updateButtonState();
        }, 250);
    }

    private void updateButtonState() {
        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < lessonsList.size() - 1);
    }
    private void applyArgsToViews(@Nullable Bundle args) {
        if (args == null) return;

        if (args.containsKey("Title") && detailTitle != null) {
            detailTitle.setText(args.getString("Title"));
        }

        if (args.containsKey("Desc") && detailDesc != null) {
            detailDesc.setText(args.getInt("Desc"));
        }

        if (args.containsKey("Image") && detailImage != null) {
            detailImage.setImageResource(args.getInt("Image"));
        }
    }

    public static LessonContents newInstance(String title, int desc, int image) {
        LessonContents fragment = new LessonContents();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putInt("Desc", desc);
        args.putInt("Image", image);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateContent(@NonNull Bundle args) {
        // If view is already created and fields are initialized -> update immediately
        if (isAdded() && getView() != null && detailTitle != null) {
            applyArgsToViews(args);
        } else {
            // If not yet created, setArguments to update when recreated
            setArguments(args);
        }
    }
}



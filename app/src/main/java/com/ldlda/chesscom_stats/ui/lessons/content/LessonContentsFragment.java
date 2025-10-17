package com.ldlda.chesscom_stats.ui.lessons.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ldlda.chesscom_stats.databinding.FragmentLessonContentsBinding;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

public class LessonContentsFragment extends Fragment {
    public static final String LESSON = "Lesson";
    private TextView detailDesc/*, detailTitle*/;
    private ImageView detailImage;
    private FragmentLessonContentsBinding binding;

    // good old bundle
    public static LessonContentsFragment newInstance(Lesson lesson) {
        LessonContentsFragment fragment = new LessonContentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(LESSON, lesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLessonContentsBinding.inflate(inflater, container, false);

        detailDesc = binding.detailDesc;
//        detailTitle = view.findViewById(R.id.detailTitle);
        detailImage = binding.detailImage;

        Bundle args = getArguments();
        applyArgsToViews(args);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void applyArgsToViews(@Nullable Bundle args) {
        if (args == null || !args.containsKey(LESSON)) return;

        // Set ClassLoader for custom Parcelable (handles older Android versions)
        args.setClassLoader(Lesson.class.getClassLoader());

        // Use type-safe getParcelable for API 33+, fallback to deprecated for older
        Lesson lesson;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            lesson = args.getParcelable(LESSON, Lesson.class);
        } else {
            lesson = args.getParcelable(LESSON);
        }

        if (lesson == null) return;

        if (detailDesc != null) {
            detailDesc.setText(lesson.dataDesc);
        }

        if (detailImage != null) {
            detailImage.setImageResource(lesson.dataImage);
        }
    }
}


package com.ldlda.chesscom_stats.ui.lessons.pager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ldlda.chesscom_stats.databinding.FragmentLessonPagerContainerBinding;
import com.ldlda.chesscom_stats.ui.lessons.LessonViewModel;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

import java.util.List;

public class LessonPagerContainerFragment extends Fragment {
    private FragmentLessonPagerContainerBinding binding;
    private List<Lesson> lessonsList;
    private int startIndex;

    private LessonViewModel viewModel;

    public static LessonPagerContainerFragment newInstance(int startIndex) {
        LessonPagerContainerFragment fragment = new LessonPagerContainerFragment();
        Bundle args = new Bundle();
        args.putInt("startIndex", startIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLessonPagerContainerBinding.inflate(inflater, container, false);

        // Get starting index
        if (getArguments() != null) {
            startIndex = getArguments().getInt("startIndex", 0);
        }

        // Setup lesson list
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        lessonsList = viewModel.Cuh.getValue();

        // Setup ViewPager2 adapter
        LessonPagerAdapter adapter = new LessonPagerAdapter(this, lessonsList);
        binding.lessonViewPager.setAdapter(adapter);

        // Add custom slide animation between lessons
        binding.lessonViewPager.setPageTransformer(new MyTransformer());

        binding.lessonViewPager.setCurrentItem(startIndex, false); // Jump to selected lesson

        // Update UI when page changes (swipe or button)
        binding.lessonViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateUI(position);
            }
        });

        // Initial UI update
        updateUI(startIndex);

        // Previous button
        binding.btnPrevious.setOnClickListener(v -> {
            int current = binding.lessonViewPager.getCurrentItem();
            if (current > 0) {
                binding.lessonViewPager.setCurrentItem(current - 1, true);
            }
        });

        // Next button
        binding.btnNext.setOnClickListener(v -> {
            int current = binding.lessonViewPager.getCurrentItem();
            if (current < lessonsList.size() - 1) {
                binding.lessonViewPager.setCurrentItem(current + 1, true);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUI(int position) {
        // Update counter
        // top haxx
        Lesson lesson = lessonsList.get(position);
        binding.lessonCounter.setText(lesson.getDataTitle());

        // Enable/disable buttons
        binding.btnPrevious.setEnabled(position > 0);
        binding.btnNext.setEnabled(position < lessonsList.size() - 1);

        int color = lesson.getColor();
        binding.btnPrevious.setTextColor(color);

        // wtf
        binding.btnPrevious.setStrokeColor(ColorStateList.valueOf(color));

        binding.btnNext.setBackgroundColor(color);

        binding.btnPrevious.setHighlightColor(color);
        binding.btnNext.setHighlightColor(color);

        binding.btnPrevious.setHintTextColor(color);

        binding.btnPrevious.setLinkTextColor(color);
    }
}

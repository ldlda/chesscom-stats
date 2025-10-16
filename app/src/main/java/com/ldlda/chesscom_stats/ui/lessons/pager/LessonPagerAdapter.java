package com.ldlda.chesscom_stats.ui.lessons.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ldlda.chesscom_stats.ui.lessons.content.LessonContentsFragment;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

import java.util.List;

public class LessonPagerAdapter extends FragmentStateAdapter {
    private final List<Lesson> lessons;

    public LessonPagerAdapter(@NonNull Fragment fragment, List<Lesson> lessons) {
        super(fragment);
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Lesson lesson = lessons.get(position);
        return LessonContentsFragment.newInstance(
                lesson.getDataTitle(),
                lesson.getDataDesc(),
                lesson.getDataImage()
        );
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

}

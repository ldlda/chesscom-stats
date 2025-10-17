package com.ldlda.chesscom_stats.ui.lessons.pager;

import android.animation.ArgbEvaluator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ldlda.chesscom_stats.R;

public class MyTransformer implements ViewPager2.PageTransformer {
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            // Page is way off-screen to the left
            page.setAlpha(0);
        } else if (position <= 0) {
            // Page is sliding out to the left (slide_out_left)
            page.setAlpha(1 + position); // 1.0 -> 0.0
            page.setTranslationX(page.getWidth() * -0.4f * position); // 0% -> -40%
            float scale = 1 - 0.05f * Math.abs(position); // 1.0 -> 0.95
            page.setScaleX(scale);
            page.setScaleY(scale);
        } else if (position <= 1) {
            // Page is sliding in from the right (slide_in_right)
            page.setAlpha(1 - position); // 0.0 -> 1.0
            page.setTranslationX(page.getWidth() * -0.4f * position); // 40% -> 0%
            float scale = 0.95f + 0.05f * (1 - position); // 0.95 -> 1.0
            page.setScaleX(scale);
            page.setScaleY(scale);
        } else {
            // Page is way off-screen to the right
            page.setAlpha(0);
        }
    }
}

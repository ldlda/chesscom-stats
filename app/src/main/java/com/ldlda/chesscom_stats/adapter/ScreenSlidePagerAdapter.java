package com.ldlda.chesscom_stats.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.ldlda.chesscom_stats.FavoritesFragment;
import com.ldlda.chesscom_stats.HallOfFameFragment;
import com.ldlda.chesscom_stats.HomeFragment;
import com.ldlda.chesscom_stats.LessonsFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 1 -> new FavoritesFragment();
            case 2 -> new HallOfFameFragment();
            case 3 -> new LessonsFragment();
            default -> new HomeFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


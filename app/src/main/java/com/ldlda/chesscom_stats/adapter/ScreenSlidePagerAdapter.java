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
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoritesFragment();
            case 2:
                return new HallOfFameFragment();
            case 3:
                return new LessonsFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


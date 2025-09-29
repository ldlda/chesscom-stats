package com.ldlda.chesscom_stats;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ldlda.chesscom_stats.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private static final String KEY_SELECTED = "selectedItemId";
    private static final String TAG_HOME = "tab:home";
    private static final String TAG_HALL = "tab:hall";
    private static final String TAG_FAV = "tab:fav";
    private static final String TAG_LESSONS = "tab:lessons";
    private MediaPlayer backgroundSong;

    // Cached fragment references for show/hide pattern
    private Fragment homeFragment, hallFragment, favoritesFragment, lessonsFragment, currentFragment;
    private int selectedItemId = R.id.home;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        Toolbar myToolbar = binding.myToolbar;
        setSupportActionBar(myToolbar);

        // Bottom navigation handling using show/hide (create fragments once)
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(KEY_SELECTED, R.id.home);
        }

        ensureFragments();
        showSelected(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuId = item.getItemId();
            Fragment target = fragmentFor(menuId);
            if (target != null && target != currentFragment) {
                getSupportFragmentManager()
                        .popBackStack("gay", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .show(target)
                        .commit();
                currentFragment = target;
                selectedItemId = menuId;
            }
            return true;
        });

        // Keep BottomNavigationView selection in sync
        if (bottomNavigationView.getSelectedItemId() != selectedItemId) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        // Music
        backgroundSong = MediaPlayer.create(this, R.raw.open_sky);
        backgroundSong.setLooping(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: triggered");
        if (backgroundSong != null && !backgroundSong.isPlaying()) {
            backgroundSong.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Only release the song when you quit out the app completely
        if (backgroundSong != null) {
            backgroundSong.release();
            backgroundSong = null;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (backgroundSong != null && backgroundSong.isPlaying()) {
            backgroundSong.pause();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED, selectedItemId);
    }

    private void ensureFragments() {
        var fm = getSupportFragmentManager();
        var tx = fm.beginTransaction();

        homeFragment = fm.findFragmentByTag(TAG_HOME);
        hallFragment = fm.findFragmentByTag(TAG_HALL);
        favoritesFragment = fm.findFragmentByTag(TAG_FAV);
        lessonsFragment = fm.findFragmentByTag(TAG_LESSONS);

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            tx.add(R.id.fragment_container, homeFragment, TAG_HOME).hide(homeFragment);
        }
        if (hallFragment == null) {
            hallFragment = new HallOfFameFragment();
            tx.add(R.id.fragment_container, hallFragment, TAG_HALL).hide(hallFragment);
        }
        if (favoritesFragment == null) {
            favoritesFragment = new FavoritesFragment();
            tx.add(R.id.fragment_container, favoritesFragment, TAG_FAV).hide(favoritesFragment);
        }
        if (lessonsFragment == null) {
            lessonsFragment = new LessonsFragment();
            tx.add(R.id.fragment_container, lessonsFragment, TAG_LESSONS).hide(lessonsFragment);
        }
        tx.commitNow();
    }

    private void showSelected(int menuId) {
        Fragment target = fragmentFor(menuId);
        if (target == null) target = homeFragment;
        var tx = getSupportFragmentManager().beginTransaction();
        if (homeFragment != null) tx.hide(homeFragment);
        if (hallFragment != null) tx.hide(hallFragment);
        if (favoritesFragment != null) tx.hide(favoritesFragment);
        if (lessonsFragment != null) tx.hide(lessonsFragment);
        tx.show(target).commitNow();
        currentFragment = target;
    }

    private Fragment fragmentFor(int menuId) {
        if (menuId == R.id.home) return homeFragment;
        if (menuId == R.id.leaderboards) return hallFragment;
        if (menuId == R.id.favorites) return favoritesFragment;
        if (menuId == R.id.lessons) return lessonsFragment;
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Using if-else instead of switch due to resource ID compilation constraints (switch requires constant expressions)
        if (id == R.id.menu_about_us) {
            Toast.makeText(this, "Group 14 on topic 25: chess.com stats", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

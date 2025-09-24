package com.ldlda.chesscom_stats;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ldlda.chesscom_stats.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer backgroundSong;

    // Cached fragment references for show/hide pattern
    private Fragment homeFragment;
    private Fragment hallFragment;
    private Fragment favoritesFragment;
    private Fragment lessonsFragment;
    private Fragment currentFragment;

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

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            hallFragment = new HallOfFameFragment();
            favoritesFragment = new FavoritesFragment();
            lessonsFragment = new LessonsFragment();
            currentFragment = homeFragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment, "home")
                    .add(R.id.fragment_container, hallFragment, "hall").hide(hallFragment)
                    .add(R.id.fragment_container, favoritesFragment, "favorites").hide(favoritesFragment)
                    .add(R.id.fragment_container, lessonsFragment, "lessons").hide(lessonsFragment)
                    .commit();
        } else {
            homeFragment = getSupportFragmentManager().findFragmentByTag("home");
            hallFragment = getSupportFragmentManager().findFragmentByTag("hall");
            favoritesFragment = getSupportFragmentManager().findFragmentByTag("favorites");
            lessonsFragment = getSupportFragmentManager().findFragmentByTag("lessons");
            if (homeFragment != null && homeFragment.isVisible()) currentFragment = homeFragment;
            else if (hallFragment != null && hallFragment.isVisible()) currentFragment = hallFragment;
            else if (favoritesFragment != null && favoritesFragment.isVisible()) currentFragment = favoritesFragment;
            else if (lessonsFragment != null && lessonsFragment.isVisible()) currentFragment = lessonsFragment;
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuId = item.getItemId();
            Fragment target;
            if (menuId == R.id.leaderboards) {
                target = hallFragment;
            } else if (menuId == R.id.favorites) {
                target = favoritesFragment;
            } else if (menuId == R.id.lessons) {
                target = lessonsFragment;
            } else {
                target = homeFragment;
            }
            if (target != null && target != currentFragment) {
                getSupportFragmentManager().beginTransaction()
                        .hide(currentFragment)
                        .show(target)
                        .commit();
                currentFragment = target;
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

        // Music
        backgroundSong = MediaPlayer.create(this, R.raw.open_sky);
        backgroundSong.setLooping(true);
        backgroundSong.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundSong != null) {
            backgroundSong.release();
            backgroundSong = null;
        }
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_about_us) {
            Toast.makeText(this, "Group 14 on topic 25: chess.com stats", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

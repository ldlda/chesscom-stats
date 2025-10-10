package com.ldlda.chesscom_stats;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ldlda.chesscom_stats.databinding.ActivityMainBinding;
import com.ldlda.chesscom_stats.ui.favorites.FavoritesFragment;
import com.ldlda.chesscom_stats.ui.home.HomeFragment;
import com.ldlda.chesscom_stats.ui.leaderboards.LeaderboardsFragment;
import com.ldlda.chesscom_stats.ui.lessons.LessonsFragment;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_SELECTED = "selectedItemId";
    private final String TAG = "MainActivity";
    private MediaPlayer backgroundSong;

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

//        NavController navController = Navigation.findNavController(binding.fragmentContainer);

        if (savedInstanceState == null) {
            replaceTo(fragmentFor(R.id.home));
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuId = item.getItemId();
            Fragment target = fragmentFor(menuId);

            replaceTo(target);
            selectedItemId = menuId;
            return true;
        });

        // Keep BottomNavigationView selection in sync
        if (bottomNavigationView.getSelectedItemId() != selectedItemId) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

        // Music
        Context context = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? createAttributionContext("audioPlayback") : this;

        backgroundSong = MediaPlayer.create(context, R.raw.open_sky);
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

    private void replaceTo(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private Fragment fragmentFor(int menuId) {
        if (menuId == R.id.home) return new HomeFragment();
        if (menuId == R.id.leaderboards) return new LeaderboardsFragment();
        if (menuId == R.id.favorites) return new FavoritesFragment();
        if (menuId == R.id.lessons) return new LessonsFragment();
        return new HomeFragment();
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

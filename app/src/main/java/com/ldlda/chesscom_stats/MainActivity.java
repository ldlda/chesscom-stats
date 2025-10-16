package com.ldlda.chesscom_stats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ldlda.chesscom_stats.databinding.ActivityMainBinding;
import com.ldlda.chesscom_stats.store.GlobalDB;
import com.ldlda.chesscom_stats.util.RepoProvider;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private MediaPlayer backgroundSong;

    private NavController navController;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Add a callback that's called when the splash screen is animating to the
        // app content.
        // on 31 only LMAO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
                final ObjectAnimator Zoom = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.SCALE_Y,
                        1, 5
                );
                final ObjectAnimator Zoom2 = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.SCALE_X,
                        1, 5
                );

                final ObjectAnimator Fade = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.ALPHA,
                        1,
                        0
                );
                AnimatorSet anim = new AnimatorSet();
                anim.play(Zoom2).with(Zoom).with(Fade);
                anim.setInterpolator(new LinearOutSlowInInterpolator()); // <-- changeable (ts so cool)
                anim.setDuration(500L);

                // Call SplashScreenView.remove at the end of your custom animation.
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove();
                    }
                });

                // Run your animation.
                anim.start();
            });
        }

        // Set up toolbar
        Toolbar myToolbar = binding.myToolbar;
        setSupportActionBar(myToolbar);

        // Setup Navigation Component
        var navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Setup automatic bottom nav visibility based on destination
            setupBottomNavVisibility(navController);
        }
        // Bottom navigation with Navigation Component
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        if (navController != null) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController, false);
        }

        GlobalDB.initDb(getApplicationContext());
        RepoProvider.setupAppContext(getApplicationContext());

        // Music
        Context context = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? createAttributionContext("audioPlayback") : this;

        backgroundSong = MediaPlayer.create(context, R.raw.open_sky);
        backgroundSong.setLooping(true);
    }

    /**
     * Automatically hide/show bottom navigation based on which fragment is showing.
     * Top-level destinations (Home, Leaderboards, Favorites, Lessons) show bottom nav.
     * Detail destinations (PlayerSearch, Clubs, FidePredict, Puzzle) hide bottom nav.
     */
    private void setupBottomNavVisibility(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();

            // Top-level destinations: SHOW bottom nav
            if (destinationId == R.id.home ||
                    destinationId == R.id.leaderboards ||
                    destinationId == R.id.favorites ||
                    destinationId == R.id.lessons) {

                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
            // Detail destinations: HIDE bottom nav
            else if (destinationId == R.id.playerSearchFragment ||
                    destinationId == R.id.clubFragment ||
                    destinationId == R.id.fidePredictFragment ||
                    destinationId == R.id.puzzleFragment) {

                binding.bottomNavigation.setVisibility(View.GONE);
            }
            // Default: show (safe fallback)
            else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
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
        binding = null; // pair with onStart
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
        // Navigation Component handles state automatically
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

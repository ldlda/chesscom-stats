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
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ldlda.chesscom_stats.databinding.ActivityMainBinding;
import com.ldlda.chesscom_stats.store.GlobalDB;
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

    private static final String TAG_LESSON_CONTENT = "tab:lesson_content";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen s = SplashScreen.installSplashScreen(this);
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

        // Bottom navigation handling using show/hide (create fragments once)
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(KEY_SELECTED, R.id.home);
        }

//        NavController navController = Navigation.findNavController(binding.fragmentContainer);

        if (savedInstanceState == null) {
            replaceTo(fragmentFor(R.id.home));
        }

        GlobalDB.initDb(getApplicationContext());

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
    private void ensureFragments() {
        var fm = getSupportFragmentManager();
        var tx = fm.beginTransaction();

        homeFragment = fm.findFragmentByTag(TAG_HOME);
        hallFragment = fm.findFragmentByTag(TAG_HALL);
        favoritesFragment = fm.findFragmentByTag(TAG_FAV);
        lessonsFragment = fm.findFragmentByTag(TAG_LESSONS);
        lessonContentFragment = fm.findFragmentByTag(TAG_LESSON_CONTENT);


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
        if (lessonContentFragment == null) {
            lessonContentFragment = new LessonContents();
            tx.add(R.id.fragment_container, lessonContentFragment, TAG_LESSON_CONTENT)
                    .hide(lessonContentFragment);
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
        if (lessonContentFragment != null) tx.hide(lessonContentFragment);   //added
        tx.show(target).commitNow();
        currentFragment = target;
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
    public void showLessonContent(Bundle args) {
        if (lessonContentFragment == null) {
            lessonContentFragment = new LessonContents();
            lessonContentFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .hide(currentFragment)
                    .add(R.id.fragment_container, lessonContentFragment, TAG_LESSON_CONTENT)
                    .show(lessonContentFragment)
                    .addToBackStack("lesson_content_back")
                    .commit();
        } else {
            // DO NOT call setArguments() on an already added fragment
            if (lessonContentFragment instanceof LessonContents) {
                ((LessonContents) lessonContentFragment).updateContent(args);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .hide(currentFragment)
                    .show(lessonContentFragment)
                    .addToBackStack("lesson_content_back")
                    .commit();
        }
        currentFragment = lessonContentFragment;
    }
}

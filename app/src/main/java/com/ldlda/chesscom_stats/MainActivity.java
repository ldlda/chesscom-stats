package com.ldlda.chesscom_stats;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ldlda.chesscom_stats.adapter.ScreenSlidePagerAdapter;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer backgroundSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Available tabs
        final String[] tabTitles = {
                getString(R.string.home),
                getString(R.string.fav),
                getString(R.string.hall_fame),
                getString(R.string.lesson)
        };

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(tabTitles[position])
        ).attach();

        // Adding the menu toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Music
        backgroundSong = MediaPlayer.create(this,R.raw.open_sky);
        backgroundSong.setLooping(true);
        backgroundSong.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (backgroundSong != null && backgroundSong.isPlaying()) {
            backgroundSong.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundSong != null) {
            backgroundSong.release();
            backgroundSong = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
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

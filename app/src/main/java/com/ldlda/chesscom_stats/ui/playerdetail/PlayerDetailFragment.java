package com.ldlda.chesscom_stats.ui.playerdetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.databinding.FragmentPlayerDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.HttpUrl;

public class PlayerDetailFragment extends Fragment {
    public static final String ARG_USERNAME = "username";

    private FragmentPlayerDetailBinding binding;
    private PlayerDetailViewModel viewModel;

    public static PlayerDetailFragment newInstance(String username) {
        PlayerDetailFragment f = new PlayerDetailFragment();
        Bundle b = new Bundle();
        b.putString(ARG_USERNAME, username);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerDetailBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayerDetailViewModel.class);

        String username = null;
        Bundle args = getArguments();
        if (args != null) username = args.getString(ARG_USERNAME);
        if (!TextUtils.isEmpty(username)) {
            viewModel.load(username);
        }

        final ImageView avatar = binding.playerDetailAvatar;
        final TextView usernameView = binding.playerDetailUsername;
        final TextView nameView = binding.playerDetailName;
        final TextView statsView = binding.playerDetailStats;

        viewModel.getPlayer().observe(getViewLifecycleOwner(), player -> {
            if (player == null) return;
            usernameView.setText(player.getUsername());
            // Avatar
            HttpUrl avatarUri = player.getAvatarUrl();
            if (avatarUri != null) {
                Picasso.get()
                        .load(avatarUri.toString())
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.ic_person);
            }

            if (!TextUtils.isEmpty(player.getName())) {
                nameView.setText(player.getName());
            }

            StringBuilder stats = new StringBuilder();
            if (player.getTitle() != null) {
                stats.append(getString(R.string.player_title)).append(": ").append(player.getTitle()).append("\n");
            }
            if (player.getCountryInfo() != null) {
                stats.append(getString(R.string.player_country)).append(": ")
                        .append(player.getCountryInfo().getName()).append("\n");
            }
            stats.append(getString(R.string.player_joined)).append(": ")
                    .append(formatInstant(player.getJoined())).append("\n");

            stats.append(getString(R.string.player_last_online)).append(": ")
                    .append(formatInstant(player.getLastOnline())).append("\n");

            if (!TextUtils.isEmpty(player.getStatus())) {
                stats.append(getString(R.string.player_status)).append(": ")
                        .append(player.getStatus());
            }
            if (player.getPlayerStats() != null && player.getPlayerStats().getBlitz() != null) {
                var blitz = player.getPlayerStats().getBlitz();
                stats.append("\nBlitz (3 minute):\n");
                if (blitz.getBest() != null) {
                    stats.append("\tBest ELO: ").append(blitz.getBest().getRating()).append("\n");
                }
                stats.append("\tGames won: ").append(blitz.getRecord().getWin()).append("\n");
            }

            statsView.setText(stats.toString());

        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.playerDetailProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.playerDetailContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        return binding.getRoot();

    }

    private String formatInstant(java.time.Instant instant) {
        if (instant == null) return "";
        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return formatter.format(date);
    }
}

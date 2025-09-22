package com.ldlda.chesscom_stats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.LeaderboardEntry;
import com.squareup.picasso.Picasso;

public class HallOfFameAdapter extends ListAdapter<LeaderboardEntry, HallOfFameAdapter.PlayerViewHolder> {

    public interface OnPlayerClickListener {
        void onPlayerClick(LeaderboardEntry player);
    }

    private final OnPlayerClickListener listener;

    public HallOfFameAdapter(OnPlayerClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<LeaderboardEntry> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull LeaderboardEntry a, @NonNull LeaderboardEntry b) {
                    // Prefer playerId if unique; fallback to username
                    return a.getPlayerId() == b.getPlayerId() || a.getUsername().equalsIgnoreCase(b.getUsername());
                }

                @Override
                public boolean areContentsTheSame(@NonNull LeaderboardEntry a, @NonNull LeaderboardEntry b) {
                    return a.equals(b);
                }
            };

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        LeaderboardEntry player = getItem(position);
        holder.username.setText(player.getUsername());
        holder.rank.setText("# " + player.getRank());
        holder.rating.setText(String.valueOf(player.getElo()));

        String avatar = player.getAvatarUrl() != null ? player.getAvatarUrl().toString() : null;
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_person);
        }
        holder.itemView.setOnClickListener(v -> listener.onPlayerClick(player));
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username, rank, rating;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.player_avatar);
            username = itemView.findViewById(R.id.player_username);
            rank = itemView.findViewById(R.id.player_rank);
            rating = itemView.findViewById(R.id.player_rating);
        }
    }
}

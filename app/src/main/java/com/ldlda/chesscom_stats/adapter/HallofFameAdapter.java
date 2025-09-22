package com.ldlda.chesscom_stats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import com.ldlda.chesscom_stats.R;

public class HallofFameAdapter extends RecyclerView.Adapter<HallofFameAdapter.PlayerViewHolder> {
    public interface OnPlayerClickListener {
        void onPlayerClick(Player player);
    }
    public static class Player {
        public String username;
        public String avatarUrl;
        public int rating;
        public Player(String username, String avatarUrl, int rating) {
            this.username = username;
            this.avatarUrl = avatarUrl;
            this.rating = rating;
        }
    }
    private List<Player> players;
    private OnPlayerClickListener listener;
    public HallofFameAdapter(List<Player> players, OnPlayerClickListener listener) {
        this.players = players;
        this.listener = listener;
    }
    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.username.setText(player.username);
        holder.rank.setText("#" + (position + 1));
        holder.rating.setText(String.valueOf(player.rating));
        com.squareup.picasso.Picasso.get().load(player.avatarUrl).placeholder(R.drawable.ic_person).into(holder.avatar);
        holder.itemView.setOnClickListener(v -> listener.onPlayerClick(player));
    }
    @Override
    public int getItemCount() {
        return players.size();
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
    //For future feature etc: swiping up the screen to refresh the list
    public void updatePlayers(List<Player> newPlayers) {
        this.players = newPlayers;
        notifyDataSetChanged();
    }
}

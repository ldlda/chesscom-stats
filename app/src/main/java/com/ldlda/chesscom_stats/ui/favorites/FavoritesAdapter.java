package com.ldlda.chesscom_stats.ui.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends ListAdapter<FavoritesAdapter.FavoritePlayer, FavoritesAdapter.FavViewHolder> {
    private final OnFavoriteClickListener listener;
    private static final DiffUtil.ItemCallback<FavoritePlayer> DIFF = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavoritePlayer oldItem, @NonNull FavoritePlayer newItem) {
            return oldItem.username.equals(newItem.username);
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavoritePlayer oldItem, @NonNull FavoritePlayer newItem) {
            return oldItem.equals(newItem);
        }
    };
    public FavoritesAdapter(OnFavoriteClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        FavoritePlayer player = getItem(position);
        holder.usernameText.setText(player.username);
        holder.titleText.setText("Title: " + player.title);

        // Convert epoch time to a readable date format
        try {
            long epochTime = Long.parseLong(player.lastOnlDate);
            String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(epochTime * 1000));
            holder.lastOnlineText.setText("Last online: " + formattedDate);
        } catch (NumberFormatException e) {
            holder.lastOnlineText.setText("Last online: --");
        }

        holder.ratingText.setText("Rating: " + player.rating);

        holder.removeBtn.setOnClickListener(v -> listener.onRemoveClicked(player.username));
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(player.username));

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in));
    }

    public void updateData(List<FavoritePlayer> newFavorites) {
        submitList(newFavorites);
    }

    public interface OnFavoriteClickListener {
        void onRemoveClicked(String username);
        void onItemClicked(String username);
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView titleText;
        TextView lastOnlineText;
        TextView ratingText;
        MaterialButton removeBtn;

        FavViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            titleText = itemView.findViewById(R.id.title_text);
            lastOnlineText = itemView.findViewById(R.id.last_online_text);
            ratingText = itemView.findViewById(R.id.rating_text);
            removeBtn = itemView.findViewById(R.id.remove_btn);
        }
    }

    public static class FavoritePlayer {
        public final String username;
        public final String title;
        public final String lastOnlDate;
        public final int rating;

        public FavoritePlayer(String username, String title, String lastOnlDate, int rating) {
            this.username = username;
            this.title = title;
            this.lastOnlDate = lastOnlDate;
            this.rating = rating;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavoritePlayer that = (FavoritePlayer) o;
            return rating == that.rating &&
                    username.equals(that.username) &&
                    title.equals(that.title) &&
                    lastOnlDate.equals(that.lastOnlDate);
        }

        @Override
        public int hashCode() {
            int result = username.hashCode();
            result = 31 * result + title.hashCode();
            result = 31 * result + lastOnlDate.hashCode();
            result = 31 * result + rating;
            return result;
        }
    }
}

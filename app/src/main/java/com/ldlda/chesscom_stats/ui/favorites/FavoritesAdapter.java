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

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FavoritesAdapter extends ListAdapter<UserFavoriteModel, FavoritesAdapter.FavViewHolder> {
    private static final DiffUtil.ItemCallback<UserFavoriteModel> DIFF = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserFavoriteModel oldItem, @NonNull UserFavoriteModel newItem) {
            return oldItem.userId == newItem.userId && oldItem.username.equals(newItem.username);
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserFavoriteModel oldItem, @NonNull UserFavoriteModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final OnFavoriteClickListener listener;

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
        UserFavoriteModel model = getItem(position);
        holder.usernameText.setText(model.username);

        // Title
        if (model.title != null) {
            holder.titleText.setText("Title: " + model.title.name());
            holder.titleText.setVisibility(View.VISIBLE);
        } else {
            holder.titleText.setText("Title: --");
            holder.titleText.setVisibility(View.VISIBLE);
        }

        // Last online
        if (model.lastLoginTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            holder.lastOnlineText.setText("Last online: " + model.lastLoginTime.atZone(java.time.ZoneId.systemDefault()).format(formatter));
            holder.lastOnlineText.setVisibility(View.VISIBLE);
        } else {
            holder.lastOnlineText.setText("Last online: Loading...");
            holder.lastOnlineText.setVisibility(View.VISIBLE);
        }

        holder.removeBtn.setOnClickListener(v -> listener.onRemoveClicked(model.username));
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(model.username));

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in));
    }

    public void updateData(List<UserFavoriteModel> newFavorites) {
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
        MaterialButton removeBtn;

        FavViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            titleText = itemView.findViewById(R.id.title_text);
            lastOnlineText = itemView.findViewById(R.id.last_online_text);
            removeBtn = itemView.findViewById(R.id.remove_btn);
        }
    }
}

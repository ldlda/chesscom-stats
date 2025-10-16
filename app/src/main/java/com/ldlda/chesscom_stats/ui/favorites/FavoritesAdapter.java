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

import java.util.List;

public class FavoritesAdapter extends ListAdapter<String, FavoritesAdapter.FavViewHolder> {
    private static final DiffUtil.ItemCallback<String> DIFF = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
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
        String username = getItem(position);
        holder.usernameText.setText(username);

        holder.removeBtn.setOnClickListener(v -> listener.onRemoveClicked(username));
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(username));

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in));
    }

    public void updateData(List<String> newFavorites) {
        submitList(newFavorites);
    }

    public interface OnFavoriteClickListener {
        void onRemoveClicked(String username);

        void onItemClicked(String username);
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        MaterialButton removeBtn;

        FavViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            removeBtn = itemView.findViewById(R.id.remove_btn);
        }
    }
}

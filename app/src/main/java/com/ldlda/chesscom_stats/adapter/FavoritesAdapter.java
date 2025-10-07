package com.ldlda.chesscom_stats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

import com.ldlda.chesscom_stats.R;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavViewHolder> {

    private List<String> favorites = new ArrayList<>();
    private final OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onRemoveClicked(String username);
        void onItemClicked(String username);
    }

    public FavoritesAdapter(List<String> favorites, OnFavoriteClickListener listener) {
        this.favorites = favorites;
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
        String username = favorites.get(position);
        holder.usernameText.setText(username);

        holder.removeBtn.setOnClickListener(v -> listener.onRemoveClicked(username));
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(username));

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updateData(List<String> newFavorites) {
        this.favorites = newFavorites;
        notifyDataSetChanged();
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        MaterialButton removeBtn;

        FavViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            removeBtn = itemView.findViewById(R.id.remove_btn);
        }
    }
}

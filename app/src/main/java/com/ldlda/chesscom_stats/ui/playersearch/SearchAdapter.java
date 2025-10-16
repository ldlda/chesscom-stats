package com.ldlda.chesscom_stats.ui.playersearch;

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
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem;
import com.ldlda.chesscom_stats.api.data.search.autocomplete.UserView;
import com.ldlda.chesscom_stats.databinding.ItemPlayerSearchBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchAdapter extends ListAdapter<SearchItem, SearchAdapter.PlayerViewHolder> {
    private static final DiffUtil.ItemCallback<SearchItem> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull SearchItem a, @NonNull SearchItem b) {
                    // Prefer playerId if unique; fallback to username
                    UserView userViewB = b.getUserView();
                    UserView userViewA = a.getUserView();
                    return userViewA.getUserId() == userViewB.getUserId() || userViewA.getUsername().equalsIgnoreCase(userViewB.getUsername());
                }

                @Override
                public boolean areContentsTheSame(@NonNull SearchItem a, @NonNull SearchItem b) {
                    return a.equals(b);
                }
            };
    private final OnPlayerClickListener listener;

    public SearchAdapter(OnPlayerClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = ItemPlayerSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot();
        return new PlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        SearchItem player = getItem(position);
        UserView userView = player.getUserView();
        holder.username.setText(userView.getUsername());
        String name = Stream.of(userView.getFirstName(), userView.getLastName())
                .filter(Objects::nonNull).collect(Collectors.joining(" ")).trim();
        holder.name.setText(String.format("Name: %s", name.isBlank() ? "N/A" : name));

        String avatar = player.getUserView().getAvatar().toString();
        if (!avatar.isEmpty()) {
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

    public interface OnPlayerClickListener {
        void onPlayerClick(SearchItem player);
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username;
        TextView name;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView); // sacred super never toucher
            ItemPlayerSearchBinding binding = ItemPlayerSearchBinding.bind(itemView);
            avatar = binding.playerAvatar;
            username = binding.playerUsername;
            name = binding.playerDetailName;
        }
    }
}

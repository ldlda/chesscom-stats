package com.ldlda.chesscom_stats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.java_api.ClubData;
import java.util.ArrayList;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private final List<ClubData> clubs = new ArrayList<>();

    public void setClubs(List<ClubData> clubList) {
        clubs.clear();
        clubs.addAll(clubList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_club, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        ClubData club = clubs.get(position);
        holder.clubName.setText(club.name);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.club_name);
        }
    }
}

package com.ldlda.chesscom_stats.ui.clubs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.java_api.ClubData;

import java.util.ArrayList;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final List<ClubData> clubs = new ArrayList<>();

    public void setClubs(List<ClubData> clubList) {
        clubs.clear();
        clubs.addAll(clubList);
        notifyDataSetChanged();
    }

    public void addClub(ClubData club) {
        clubs.add(club);
        notifyItemInserted(clubs.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_club, parent, false);
            return new ClubViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_club_loading, parent, false);
            return new ClubLoadViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (holder instanceof ClubViewHolder vh) {
            ClubData club = clubs.get(position);
            if (club != null) {

                vh.clubName.setText(club.name);

                String memberCountTxt =
                        context.getResources().getString(R.string.clubMember) +
                                ": " +
                                club.membersCount;

                vh.clubMembersCount.setText(memberCountTxt);

                // Hacky  lol
                String clubVisTxt = club.visibility.equals("public") ?
                        context.getResources().getString(R.string.clubPublic) :
                        context.getResources().getString(R.string.clubPrivate);


                vh.clubVisibility.setText(clubVisTxt);

                // Could have done better but I'm too lazy
                if (clubVisTxt.equals("Public")) {
                    vh.clubVisibility.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    vh.clubVisibility.setTextColor(context.getResources().getColor(R.color.red));
                }

                vh.clubURI.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(club.joinRequest));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Can't redirect to club", Toast.LENGTH_SHORT).show();
                        Log.e("ClubAdapter", "Invalid club URL: " + club.joinRequest, e);
                    }
                });


                Glide.with(context)
                        .load(club.icon)
                        .placeholder(R.drawable.baseline_home_24)
                        .error(R.drawable.ic_community)
                        .into(vh.clubAvatar);

            }
        } else {
            Toast.makeText(holder.itemView.getContext(), "CHILL", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }


    @Override
    public int getItemViewType(int position) {
        return clubs.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        TextView clubMembersCount;
        TextView clubVisibility;
        ImageView clubAvatar;
        MaterialButton clubURI;

        ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.clubName);
            clubMembersCount = itemView.findViewById(R.id.clubMemberCount);
            clubVisibility = itemView.findViewById(R.id.clubVisibility);
            clubURI = itemView.findViewById(R.id.clubURI);
            clubAvatar = itemView.findViewById(R.id.club_avatar);
        }
    }

    static class ClubLoadViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ClubLoadViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.prog_bar);
        }
    }
}

package com.ldlda.chesscom_stats.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.java_api.ClubData;

import org.w3c.dom.Text;

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
        if (viewType == VIEW_TYPE_ITEM){
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_club, parent, false);
            return new ClubViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_club_loading, parent, false);
            return new ClubLoadViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (holder instanceof ClubViewHolder){
            ClubData club = clubs.get(position);
            if (club != null) {
                ((ClubViewHolder) holder).clubName.setText(club.name);

                String memberCountTxt =
                        context.getResources().getString(R.string.clubMember) +
                                ": " +
                                club.membersCount;

                ((ClubViewHolder) holder).clubMembersCount.setText(memberCountTxt);

                // Hacky  lol
                String clubVisTxt = club.visibility.equals("public")?
                context.getResources().getString(R.string.clubPublic):
                context.getResources().getString(R.string.clubPrivate);

                ((ClubViewHolder) holder).clubVisiblity.setText(clubVisTxt);
            }
        }else {
            Toast.makeText(holder.itemView.getContext(),"CHILL",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }


    @Override
    public int getItemViewType(int position) {
        return  clubs.get(position) == null? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        TextView clubMembersCount;
        TextView clubVisiblity;
        ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.clubName);
            clubMembersCount = itemView.findViewById(R.id.clubMemberCount);
            clubVisiblity = itemView.findViewById(R.id.clubVisibility);
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

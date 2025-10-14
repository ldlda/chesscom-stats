package com.ldlda.chesscom_stats.ui.lessons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.LessonContents;
import com.ldlda.chesscom_stats.MainActivity;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder> {

    private final Context context;
    private List<Lesson> dataList;

    public void setSearchList(List<Lesson> dataSearchList) {
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public LessonAdapter(Context context, List<Lesson> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public LessonAdapter(List<String> lessons, OnLessonClickListener listener) { // Changed from String[] to List<String>
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recImage.setImageResource(dataList.get(position).getDataImage());
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(context.getString(dataList.get(position).getDataDesc()));
        holder.recLang.setText(dataList.get(position).getDataLang());

        holder.recCard.setOnClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Bundle args = new Bundle();
                args.putInt("Image", dataList.get(currentPosition).getDataImage());
                args.putString("Title", dataList.get(currentPosition).getDataTitle());
                args.putInt("Desc", dataList.get(currentPosition).getDataDesc());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).showLessonContent(args);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnLessonClickListener {
        void onLessonClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recImage;
        TextView recTitle, recDesc, recLang;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recLang = itemView.findViewById(R.id.recLang);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
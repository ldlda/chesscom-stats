package com.ldlda.chesscom_stats.ui.lessons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.ui.lessons.data.Lesson;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.MyViewHolder> {

    private final Context context;
    private final ToLessonContent listener;
    private List<Lesson> dataList;

    public LessonsAdapter(Context context, List<Lesson> dataList, ToLessonContent listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSearchList(List<Lesson> dataSearchList) {
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recImage.setImageResource(dataList.get(position).dataImage);
        holder.recTitle.setText(dataList.get(position).dataTitle);
        holder.recDesc.setText(context.getString(dataList.get(position).dataDesc));
        holder.recLevel.setText(dataList.get(position).dataLevel);

        holder.recCard.setOnClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.toLessonContent(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface ToLessonContent {
        void toLessonContent(int currentPosition);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recImage;
        TextView recTitle, recDesc, recLevel;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recLevel = itemView.findViewById(R.id.recLang);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}

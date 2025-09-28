package com.ldlda.chesscom_stats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Assuming R class is correctly generated in this package.
// If your R class is in a different package, this might need adjustment.
// import com.ldlda.chesscom_stats.R; 

import java.util.List; // Added for List

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final List<String> lessons; // Changed from String[] to List<String>
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(int position);
    }

    public LessonAdapter(List<String> lessons, OnLessonClickListener listener) { // Changed from String[] to List<String>
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.title.setText(lessons.get(position)); // Changed from lessons[position] to lessons.get(position)
        holder.itemView.setOnClickListener(v -> listener.onLessonClick(position));
    }

    @Override
    public int getItemCount() {
        return lessons.size(); // Changed from lessons.length to lessons.size()
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        LessonViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
        }
    }
}

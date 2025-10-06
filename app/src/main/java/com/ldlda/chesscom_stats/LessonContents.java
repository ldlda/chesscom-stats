package com.ldlda.chesscom_stats;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ldlda.chesscom_stats.R;

public class LessonContents extends Fragment {

    private TextView detailDesc, detailTitle;
    private ImageView detailImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_contents, container, false);

        detailDesc = view.findViewById(R.id.detailDesc);
        detailTitle = view.findViewById(R.id.detailTitle);
        detailImage = view.findViewById(R.id.detailImage);

        if (getArguments() != null) {
            detailTitle.setText(getArguments().getString("Title"));
            detailDesc.setText(getArguments().getInt("Desc"));
            detailImage.setImageResource(getArguments().getInt("Image"));
        }

        return view;
    }

    public static LessonContents newInstance(String title, int desc, int image) {
        LessonContents fragment = new LessonContents();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putInt("Desc", desc);
        args.putInt("Image", image);
        fragment.setArguments(args);
        return fragment;
    }
}



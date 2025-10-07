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

        Bundle args = getArguments();
        applyArgsToViews(args);

        return view;
    }

    private void applyArgsToViews(@Nullable Bundle args) {
        if (args == null) return;

        if (args.containsKey("Title") && detailTitle != null) {
            detailTitle.setText(args.getString("Title"));
        }

        if (args.containsKey("Desc") && detailDesc != null) {
            detailDesc.setText(args.getInt("Desc"));
        }

        if (args.containsKey("Image") && detailImage != null) {
            detailImage.setImageResource(args.getInt("Image"));
        }
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

    public void updateContent(@NonNull Bundle args) {
        // If view is already created and fields are initialized -> update immediately
        if (isAdded() && getView() != null && detailTitle != null) {
            applyArgsToViews(args);
        } else {
            // If not yet created, setArguments to update when recreated
            setArguments(args);
        }
    }
}



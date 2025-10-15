package com.ldlda.chesscom_stats.ui.lessons.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ldlda.chesscom_stats.databinding.FragmentLessonContentsBinding;

public class LessonContentsFragment extends Fragment {

    private TextView detailDesc/*, detailTitle*/;
    private ImageView detailImage;

    private FragmentLessonContentsBinding binding;

    public static LessonContentsFragment newInstance(String title, int desc, int image) {
        LessonContentsFragment fragment = new LessonContentsFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putInt("Desc", desc);
        args.putInt("Image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLessonContentsBinding.inflate(inflater, container, false);

        detailDesc = binding.detailDesc;
//        detailTitle = view.findViewById(R.id.detailTitle);
        detailImage = binding.detailImage;

        Bundle args = getArguments();
        applyArgsToViews(args);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void applyArgsToViews(@Nullable Bundle args) {
        if (args == null) return;

//        if (args.containsKey("Title") && detailTitle != null) {
//            detailTitle.setText(args.getString("Title"));
//        }

        if (args.containsKey("Desc") && detailDesc != null) {
            detailDesc.setText(args.getInt("Desc"));
        }

        if (args.containsKey("Image") && detailImage != null) {
            detailImage.setImageResource(args.getInt("Image"));
        }
    }
}


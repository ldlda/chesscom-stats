package com.ldlda.chesscom_stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;

public class FidePredictFragment extends Fragment {
    private final double[] WEIGHTS = {0.01267989, 0.42249432, 0.08961913};
    private final double BIAS = 2.1294016730515038e-16;

    private int PredictionFunc(int bullet, int blitz, int rapid){
        double prediction = BIAS + WEIGHTS[0] * bullet + WEIGHTS[1] * blitz + WEIGHTS[2] * rapid;
        return Math.toIntExact(Math.round(prediction));
    };

    private EditText bullet_score;
    private EditText blitz_score;
    private EditText rapid_score;
    private TextView fide_res;
    private Button get_res;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fide_predict, container, false);
        bullet_score = view.findViewById(R.id.bullet_score);
        blitz_score = view.findViewById(R.id.blitz_score);
        rapid_score = view.findViewById(R.id.rapid_score);
        fide_res = view.findViewById(R.id.fide_result);

        get_res = view.findViewById(R.id.retrieve_res);
        return view;
    }
}

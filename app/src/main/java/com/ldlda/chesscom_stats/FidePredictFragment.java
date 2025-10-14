package com.ldlda.chesscom_stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class FidePredictFragment extends Fragment {
    // Raw grad descent version
    private final double[] WEIGHTS = {0.01267989, 0.42249432, 0.08961913};
    private final double BIAS = 2.1294016730515038e-16;
    /*
    // Scikit version
    private final double[] WEIGHTS = {0.0122492, 0.42294166, 0.08959167};
    private final double BIAS = 2.1996974976942932e-16;
     */
    private final double[] MEAN_X = {2380.05522914, 2470.54602429, 2188.69408539};
    private final double[] STD_X = {358.64892717, 299.44639535, 274.0567951};
    private final double MEAN_Y = 2234.3300039169603;
    private final double STD_Y = 264.50016824539256;

    private int PredictionFunc(int bullet, int blitz, int rapid){
        // Normalize inputs
        double x_bullet = (bullet - MEAN_X[0]) / STD_X[0];
        double x_blitz = (blitz - MEAN_X[1]) / STD_X[1];
        double x_rapid = (rapid - MEAN_X[2]) / STD_X[2];

        // Predict (in normalized space)
        double y_scaled = BIAS + WEIGHTS[0] * x_bullet + WEIGHTS[1] * x_blitz + WEIGHTS[2] * x_rapid;

        // Convert back to actual rating
        double prediction = y_scaled * STD_Y + MEAN_Y;

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

        get_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bullet_val = bullet_score.getText().toString().trim();
                String blitz_val = blitz_score.getText().toString().trim();
                String rapid_val = rapid_score.getText().toString().trim();

                int bullet, blitz, rapid;
                if (bullet_val.isEmpty() || blitz_val.isEmpty() || rapid_val.isEmpty()) {
                    Toast.makeText(getContext(), "All 3 stats need to be filled my man", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    bullet = Integer.parseInt(bullet_val);
                    blitz = Integer.parseInt(blitz_val);
                    rapid = Integer.parseInt(rapid_val);

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Enter valid integer scores pal", Toast.LENGTH_SHORT).show();
                    return;
                }

                fide_res.setText("Estimated FIDE rating: \n"+PredictionFunc(bullet,blitz,rapid));
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}

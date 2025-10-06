package com.ldlda.chesscom_stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.PlayerProfile;
import com.ldlda.chesscom_stats.java_api.PlayerProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;

public class PlayerSearchFragment extends Fragment {
    private SearchView endpoints_plr_search;

    private ProgressBar search_prog;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_search, container, false);

        queue = Volley.newRequestQueue(requireContext());

        // Components
        endpoints_plr_search = view.findViewById(R.id.all_plr_search);
        search_prog = view.findViewById(R.id.prog_bar);

        // API https://api.chess.com/pub/player/
        PlayerProfile api = ApiClient.getClient().create(PlayerProfile.class);

        endpoints_plr_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String username = query.toLowerCase().trim();
                search_prog.setVisibility(View.VISIBLE);
                Log.i("SearchFrag", "Searching for: " + username);

                Call<PlayerProfileData> call = api.getPlayerProfile(username);

                call.enqueue(new retrofit2.Callback<PlayerProfileData>() {

                    @Override
                    public void onResponse(Call<PlayerProfileData> call, retrofit2.Response<PlayerProfileData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            search_prog.setVisibility(View.GONE);
                            PlayerProfileData data = response.body();

                            // Simple Toast to show result
                            Toast.makeText(requireContext(),
                                    data.username,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SearchFrag_Error", "HTTP " + response.code());
                            Toast.makeText(requireContext(),
                                    "Player not found (" + response.code() + ")",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayerProfileData> call, Throwable t) {
                        Log.e("SearchFrag_Failure", "Error: " + t.getMessage());
                        Toast.makeText(requireContext(),
                                "Request failed: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });

        endpoints_plr_search = view.findViewById(R.id.all_plr_search);
        endpoints_plr_search.setIconified(false);
        endpoints_plr_search.requestFocus();

        // Force keyboard to appear
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(endpoints_plr_search, InputMethodManager.SHOW_IMPLICIT);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

}